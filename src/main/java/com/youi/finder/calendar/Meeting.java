package com.youi.finder.calendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meeting {
	
	private String name;
	private String room;
	
	@Override
	public String toString() {
		return "Meeting [name=" + name + ", room=" + room + "]";
	}

}
