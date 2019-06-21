package com.youi.finder.google;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DateTime {
	
	private Date dateTime;
	private String timeZone;
	
	@Override
	public String toString() {
		return "DateTime [dateTime=" + dateTime + ", timeZone=" + timeZone + "]";
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

}
