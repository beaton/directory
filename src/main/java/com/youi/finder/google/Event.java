package com.youi.finder.google;

import java.util.Collection;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a Google Calendar Event (or meeting).
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
	
	private Collection<Attendee> attendees;
	
	/** This is the meeting description */
	protected String description;
	
	/** This is the meeting location/room. */
	protected String location;
	
	/** This is the title of the meeting. */
	protected String summary;
	
	/** This is the confirmation of the meeting: "confirmed" or "canceled" for example. */
	protected String status;
	
	/** This is the time the event starts */
	protected DateTime start;
	
	/** This is the time the event ends */
	protected DateTime end;
	
	/**
	 * Determine if this Event is current or otherwise (past or future).
	 * @return a boolean to indicate true where this event is currently in progress.
	 */
	public boolean isCurrent() {
		
		// Google Calendar cancelled meetings do not have dates, return false.
		if(status.equals("cancelled")) return false;
		
		// Google calendar meetings start and end are inclusive of re-occurring meetings.
		Date now = new Date(System.currentTimeMillis());
		boolean isAfterStart = now.after(this.normalizeDate(now, this.start.getDateTime()));
		boolean isBeforeEnd = now.before(this.normalizeDate(now, this.end.getDateTime()));
		return isBeforeEnd && isAfterStart;
	}
	
	/**
	 * Convenience method to normalize the dates for comparison.
	 */
	@SuppressWarnings("deprecation")
	private Date normalizeDate(Date now, Date aDate) {
		
		int hours = aDate.getHours();
		int min = aDate.getMinutes();
		int sec = aDate.getSeconds();
		
		return new Date(now.getYear(), now.getMonth(), now.getDate(), hours, min, sec);
	}
	
	public boolean isNullEvent() {
		return false;
	}
	
	public String getLocationAsString() {
		return location;
	}

	@Override
	public String toString() {
		return "Event [attendees=" + attendees + ", description=" + description + ", location=" + location
				+ ", summary=" + summary + ", status=" + status + ", start=" + start + ", end=" + end + "]";
	}
	
}
