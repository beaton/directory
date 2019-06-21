package com.youi.finder.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * This is an individual Attendee to a Calendar Event (meeting).
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attendee {
	
	private String displayName;
	private String email;
	private String responseStatus;
	
	@Override
	public String toString() {
		return "Attendee [displayName=" + displayName + ", email=" + email + ", responseStatus=" + responseStatus + "]";
	}
	

}
