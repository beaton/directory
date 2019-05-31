package com.youi.finder.calendar;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MeetingList {
	
	private List<Meeting> meetings;
	
	public MeetingList() {
		meetings = new ArrayList<Meeting>();
	}

	public List<Meeting> getMeetings() {
		return meetings;
	}

	public void setMeetings(List<Meeting> meetings) {
		this.meetings = meetings;
	}

	@Override
	public String toString() {
		return "MeetingList [meetings=" + meetings + "]";
	}
}
