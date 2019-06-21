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
import com.google.api.services.calendar.Calendar;
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
public class GoogleCalendar {

	Logger logger = Logger.getLogger(GoogleCalendar.class);

	@Value("${google.service.account.credentials}")
	private String credentialsFile;

	/**
	 * Looks up and returns a list of today's meetings for userEmail.
	 * 
	 * //Calendar List Entry summary: 307-1-1st Floor Boardroom (14) [2 TVs, HDMI,
	 * Polycom] //Calendar List Entry summary: andrew@foo.com (when no room is
	 * booked).
	 */
	public Calendar getMeetings(String userEmail) throws GeneralSecurityException, IOException, URISyntaxException {

		logger.info("Retrieve today's meetings for: " + userEmail);
		Calendar service = getCalendarService();

		String pageToken = null;
		do {
			CalendarList calendarList = service.calendarList().list()
					.setPageToken(pageToken)
					.setMaxResults(10)
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
	 * Find Google Calendar Events between the current time and end of day for emailUser.
	 */
	public com.youi.finder.google.Event getCurrentEvent(String userEmail) throws IOException {

		logger.info("GET events from Google Calendar for: " + userEmail);
		
		DateTime now = new DateTime(System.currentTimeMillis());
		DateTime endOfDay = new DateTime(getEndOfDay());
		Calendar service = getCalendarService();
		logger.debug("Retrieving events from: " + now.toString() + " to: " + endOfDay.toString());
		
		// Call Google Calendar API
		Events events = service.events().list(userEmail)
				.setTimeMin(now)
				.setTimeMax(endOfDay)
				.setMaxResults(10)
				.setPrettyPrint(true)
				.execute();
		List<Event> items = events.getItems();	
		
		return getCurrentEvent(items);
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
			if (isCurrent(nextEvent)) break;
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
	private Calendar getCalendarService() throws IOException {

		logger.info("Get the Google Calendar Service");
		HttpTransport httpTransport = new NetHttpTransport();
		JacksonFactory jsonFactory = new JacksonFactory();

		Collection<String> scopes = new ArrayList<String>();
		scopes.add(CalendarScopes.CALENDAR_EVENTS);

		InputStream credentialsJSON = GoogleCalendar.class.getClassLoader().getResourceAsStream(credentialsFile);
		GoogleCredential credential = GoogleCredential.fromStream(credentialsJSON, httpTransport, jsonFactory);
		credential = credential.createScoped(Collections.singleton(CalendarScopes.CALENDAR));
		Calendar.Builder builder = new Calendar.Builder(httpTransport, jsonFactory, null);
		builder.setApplicationName("Alexa Google Calendar Finder");
		Calendar service = builder.setHttpRequestInitializer(credential).build();

		return service;
	}

}
