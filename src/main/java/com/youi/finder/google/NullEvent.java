package com.youi.finder.google;

/**
 * Implements the null pattern for Events.
 */
public class NullEvent extends Event {
	
	public NullEvent() {
		this.location = " is not currently in any meetings";
		this.start = new DateTime();
		this.status = "available";
		this.summary = "";
		this.description = "";
	}
	
	/**
	 * Constructor converts anEvent into a NullEvent. This is useful
	 * where anEvent is corrupt or empty, which is common.
	 */
	public NullEvent(Event anEvent) {
	    this();
		this.status = anEvent.status;
		this.summary = anEvent.summary;
		this.description = anEvent.description;
	}
	
	@Override
	public boolean isNullEvent() {
		return true;
	} 
	
	/**
	 * Speech text for location.
	 */
	@Override
	public String getLocationAsSpeech() {
		return location;
	}

}
