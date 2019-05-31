package com.youi.finder.calendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Meeting {
	
	private String name;
	private String room;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	
	@Override
	public String toString() {
		return "Meeting [name=" + name + ", room=" + room + "]";
	}

}
