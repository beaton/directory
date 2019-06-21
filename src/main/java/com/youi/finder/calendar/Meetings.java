package com.youi.finder.calendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meetings {
	
	private Meeting[] meetings;
	
	public Meetings() {}
	
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
