package com.youi.finder.calendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Meetings {
	
	private Meeting[] meetings;
	
	public Meetings() {}

	public Meeting[]getMeetings() {
		return meetings;
	}

	public void setMeetings(Meeting[] meetings) {
		this.meetings = meetings;
	}
	
	public int getLength() {
		return meetings.length;
	}

	@Override
	public String toString() {
		
		StringBuffer buffer = new StringBuffer("MeetingList");
		for (int i = 0; i < meetings.length; i++) {
			buffer.append(" [ ");
			buffer.append(meetings[i]);
			buffer.append(" ]");
		}
		return buffer.toString();
	}
}
