package com.youi.finder.calendar;

import java.net.URI;

import org.apache.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;

/**
 * Calendar class represents a Spring Boot service that calls the Google
 * Calendar Service (in this case, another custom micro-service).
 * 
 * Meeting format returned from the downstream micro-service:
 * 
 * { "meetings": [ { "name": "Bogus meeting for testing", "room": "Wow" } ] }
 */
@Service
@Getter
@Setter
@ConfigurationProperties(prefix = "calendar")
public class Calendar {

	Logger logger = Logger.getLogger(Calendar.class);
	
	public String findStaff = "http://18.222.251.26:3000/find_staff/";

	/**
	 * A user can have zero or more concurrent meetings.
	 */
	public Meetings meetings;
	
	public Calendar() {}

	/**
	 * Parse the person's name and call the Google Calendar service to find the
	 * person's current meeting(s).
	 */
	public String getMeetingRoom(String name) {

		// TODO: this method is very procedural, fix it.
		
		logger.info("Searching meetings in Google Calendar for " + name);

		// Some basic error handling ...
		if (name == null | name.trim().length() == 0) {
			logger.warn("User name is not applicable");
			return "I'm sorry, you have to give a name.  Try asking, Google Calendar, where is Ken?";
		}
		
		// Split the name into first and last name.
		// TODO: consider being more robust in the future.
		String[] parts = name.split(" ");
		if ((parts.length < 2) && (parts[0].equalsIgnoreCase("ken"))) {
			logger.debug("User requested, Ken. Modifying to Ken Beaton ...");
			String update[] = {"Ken", "Beaton"};
			parts = update;
		}
		
		
		if ((parts.length < 2) && (parts[0].equalsIgnoreCase("jason"))) {
			logger.debug("User requested, Jason. Modifying to Jason Flick ...");
			String update[] = {"Jason", "Flick"};
			parts = update;
		}
		
		if (parts.length < 2) {
			logger.warn("User name is only a single name. We need first and last name.");
			return "I'm sorry, you're going to have to be more specific, I don't recognize that name.";
		} else if (parts.length > 2) {
			logger.warn("User name is more than first and last name, not supported.");
			return "I'm sorry, I don't currently support more than two names: " + name;
		}

		// If parts == 2 ...
		String meetingRoomMsg = null;
		String staff = parts[0] + "%20" + parts[1];
		URI uri = getUri(staff);

		RestTemplate restTemplate = new RestTemplate();
		meetings = restTemplate.getForObject(uri, Meetings.class);
		logger.info("Found meetings: " + meetings);

		if (meetings.getLength() == 0) {
			String msg = parts[0] + " is not currently in any meetings.";
			logger.info(msg);
			return msg;
		}

		Meeting meeting = null;
		String room = null;
		StringBuffer buffer = new StringBuffer(parts[0] + " is in");
		for (int i = 0; i < meetings.getLength(); i++) {
			meeting = meetings.getMeetings()[i];
			room = meeting.getRoom();
			buffer.append(" ");
			buffer.append(room);
		}
		meetingRoomMsg = buffer.toString();

		logger.debug("Google Calendar request URI: " + uri.toString());
		logger.debug("Found meetings: " + meetings);

		logger.info(meetingRoomMsg);
		return meetingRoomMsg;
	}

	private URI getUri(String name) {
		String parameter = name;
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.findStaff).path(parameter);
		UriComponents components = builder.build(true);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Google Calendar GET request: " + components.toUri().toString());
		}
		return components.toUri();
	}

	@Override
	public String toString() {
		return "Calendar [meetings=" + meetings + "]";
	}

}
