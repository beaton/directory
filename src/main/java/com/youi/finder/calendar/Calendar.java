package com.youi.finder.calendar;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Calendar {
	
	Logger logger = Logger.getLogger(Calendar.class);
	
	private Collection<Meeting> meetings;
	
	public Calendar() {
		meetings = new ArrayList<Meeting>();
	}
	
	/**
	 * Parse the name and call the Google Calendar service to find the person's
	 * current meeting.
	 */
	public String getMeetingRoom(String name) {
					
		// Some basic error handling ...
		if (name == null | name.trim().length() == 0) {
			return "I'm sorry, I cannot find that name.";
		}
		String[] parts = name.split(" ");
		if (parts.length < 2) {
			return "I'm sorry, you're going to have to be more specific.";
		} else if (parts.length > 2) {
			return "I'm sorry, I cannot find " + parts[0];
		}
		
		// If parts == 2 ...
		String meetingRoomMsg = null;
		String staff = parts[0] + "%20" + parts[1];
		logger.debug(staff);
		
		// Call the calendar service
		RestTemplate restTemplate = new RestTemplate();
		
		//String address = "http://18.222.251.26:3000/find_staff/" + staff;
		//URI url = new URI(address);
		//Meeting meeting = restTemplate.getForObject(url, Meeting.class);
		
		String address = "http://18.222.251.26:3000/find_staff/" + staff;		
		MeetingList response = restTemplate.getForObject(address, MeetingList.class);
		List<Meeting> meetings = response.getMeetings();
		
		logger.debug("Google Calendar request URI: " + address);
		logger.debug("Google Calendar reponse: " + response);
		logger.debug("Found meetings: " + meetings);
		
		if (meetings.isEmpty()) {
			meetingRoomMsg = parts[0] + " is not currently scheduled for any meetings.";
			logger.debug(meetingRoomMsg);
			return meetingRoomMsg;
		}
		
		Iterator<Meeting> i = meetings.iterator();
		Meeting meeting = null;
		while (i.hasNext()) {
			meeting = i.next();
			logger.debug("Found meeting: " + meeting);
			
			//TODO: hack return the first one when there are many.
			break;
		}
		
		meetingRoomMsg = parts[0] + " is in " + meeting.getRoom();
		logger.info(meetingRoomMsg);
		
		return meetingRoomMsg;
	}

	public Collection<Meeting> getMeetings() {
		return meetings;
	}

	public void setMeetings(Collection<Meeting> meetings) {
		this.meetings = meetings;
	}

	@Override
	public String toString() {
		return "Calendar [meetings=" + meetings + "]";
	}

}
