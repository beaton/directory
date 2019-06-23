package com.youi.finder.google;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import lombok.Getter;
import lombok.Setter;

@Controller
@Setter
@Getter
public class Calendar {

	Logger logger = Logger.getLogger(Calendar.class);

	@Value("${google.service.account.credentials}")
	private String credentialsFile;

	@Value("${email.host}")
	private String emailhost;
	
	/** The request is valid */
	private boolean valid = false;
	
	/** If the request is invalid, this message will be returned to sender. */
	private String errorMessage;
	
	/** Requested calendar owner's email address */
	private String emailAddress;
	
	private String firstName = "";
	private String lastName = "";

	/**
	 * Looks up and returns a list of today's meetings for userEmail.
	 * 
	 * //Calendar List Entry summary: 307-1-1st Floor Boardroom (14) [2 TVs, HDMI,
	 * Polycom] //Calendar List Entry summary: andrew@foo.com (when no room is
	 * booked).
	 */
	public com.google.api.services.calendar.Calendar getMeetings(String name)
			throws GeneralSecurityException, IOException, URISyntaxException {

		logger.info("Retrieve today's meetings for: " + name);
		com.google.api.services.calendar.Calendar service = getCalendarService();

		String pageToken = null;
		do {
			CalendarList calendarList = service.calendarList().list().setPageToken(pageToken).setMaxResults(10)
					.execute();
			List<CalendarListEntry> items = calendarList.getItems();

			for (CalendarListEntry calendarListEntry : items) {
				logger.debug("Calendar List Entry location: " + calendarListEntry.getLocation());
				logger.debug("Calendar List Entry description: " + calendarListEntry.getDescription());
				logger.debug("Calendar List Entry summary: " + calendarListEntry.getSummary());
			}
			pageToken = calendarList.getNextPageToken();
		} while (pageToken != null);

		return service;
	}

	/**
	 * Find Google Calendar Events between the current time and end of day for
	 * emailUser.
	 */
	public String getCurrentLocation(String name) throws IOException {

		logger.info("GET events from Google Calendar for: " + name);

		DateTime now = new DateTime(System.currentTimeMillis());
		DateTime endOfDay = new DateTime(getEndOfDay());
		com.google.api.services.calendar.Calendar service = getCalendarService();
		logger.debug("Retrieving events from: " + now.toString() + " to: " + endOfDay.toString());

		// Call Google Calendar API
		this.verifyName(name);
		this.setEmailAddress(name);
		
		if (!valid) {
			logger.warn("Name is not valid");
			return errorMessage;
		}

		Events events = service.events()
				.list(emailAddress)
				.setTimeMin(now)
				.setTimeMax(endOfDay)
				.setMaxResults(10)
				.setPrettyPrint(true)
				.execute();
		List<Event> items = events.getItems();

		com.youi.finder.google.Event current = getCurrentEvent(items);
		
		String speechText = null;
		if (current instanceof NullEvent) {
			speechText = firstName + current.getLocationAsString();
		} else {
			speechText = firstName + " is currently located in " + current.getLocationAsString();
		}
		return speechText;
	}

	/**
	 * Verify the name is valid for finding their Google Calendar.
	 */
	private void verifyName(String name) {
		// Some basic error handling ...
		if (name == null | name.trim().length() == 0) {
			errorMessage = "Sorry, I didn't catch that, can you try again?";
			logger.error("Warning, name is required!");
			valid = false;
		}
		valid = true;
	}

	/**
	 * Configure email address based on requested name for finding Google Calendar.
	 * Note: Google Calendar is associated with an email address for a particular organization,
	 * see application.properties for email domain configuration.
	 */
	private void setEmailAddress(String name) {
		
		name = name.trim();
		logger.info("Processing calendar request for: " + name);
		
		//Split the name into an array.
		String[] parts = name.split(" ");
		
		//If they request "Ken", assume it's me!
		if ((parts.length < 2) && (parts[0].equalsIgnoreCase("ken"))) {
			logger.debug("User requested, Ken. Modifying to Ken Beaton ...");
			String update[] = {"Ken", "Beaton"};
			parts = update;
		}
		
		if (parts.length < 2) {
			valid = false;
			logger.warn("User name is only a single name. We need first and last name.");
			this.errorMessage = "I'm sorry, I do not generally recognize single names. Try first name and last name, like a what is used in your email address.";
		} else if (parts.length > 2) {
			valid = false;
			logger.warn("User name is more than first and last name, not supported.");
			this.errorMessage = "I'm sorry, I don't currently support more than two names: " + name;
		}
		
		if (!valid) return;
		
		this.firstName = parts[0];
		this.lastName = parts[1];
		emailAddress = firstName + "." + lastName + emailhost;		
	}

	/**
	 * Find the current meeting in a list of meetings.
	 */
	private com.youi.finder.google.Event getCurrentEvent(List<Event> events) throws IOException {

		Event event = null;
		com.youi.finder.google.Event nextEvent = null;

		ObjectMapper mapper = new ObjectMapper();
		Iterator<Event> i = events.iterator();
		while (i.hasNext()) {
			event = i.next();
			logger.debug("JSON Event: " + event.toString());

			// Transform JSON format into object.
			nextEvent = mapper.readValue(event.toString(), com.youi.finder.google.Event.class);

			// Check if this meeting is the current meeting.
			if (isCurrent(nextEvent))
				break;
		}
		if (nextEvent == null) {
			nextEvent = new NullEvent();
		}
		return nextEvent;
	}

	/**
	 * Check if this meeting is current and not in the past of future.
	 */
	private boolean isCurrent(com.youi.finder.google.Event anEvent) {
		return anEvent.isCurrent();
	}

	/**
	 * Calculate the end of day.
	 * 
	 * @return Date representing midnight today.
	 */
	private Date getEndOfDay() {
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		int year = calendar.get(java.util.Calendar.YEAR);
		int month = calendar.get(java.util.Calendar.MONTH);
		int day = calendar.get(java.util.Calendar.DATE);
		calendar.set(year, month, day, 23, 59, 59);
		return calendar.getTime();
	}

	/**
	 * Establish a connection to the Google Calendar Service.
	 */
	private com.google.api.services.calendar.Calendar getCalendarService() throws IOException {

		logger.info("Get the Google Calendar Service");
		HttpTransport httpTransport = new NetHttpTransport();
		JacksonFactory jsonFactory = new JacksonFactory();

		Collection<String> scopes = new ArrayList<String>();
		scopes.add(CalendarScopes.CALENDAR_EVENTS);

		InputStream credentialsJSON = Calendar.class.getClassLoader().getResourceAsStream(credentialsFile);
		GoogleCredential credential = GoogleCredential.fromStream(credentialsJSON, httpTransport, jsonFactory);
		credential = credential.createScoped(Collections.singleton(CalendarScopes.CALENDAR));
		com.google.api.services.calendar.Calendar.Builder builder = new com.google.api.services.calendar.Calendar.Builder(
				httpTransport, jsonFactory, null);
		builder.setApplicationName("Alexa Google Calendar Finder");
		com.google.api.services.calendar.Calendar service = builder.setHttpRequestInitializer(credential).build();

		return service;
	}

}
