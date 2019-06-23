package com.youi.finder.google;

/**
 * Implements the null pattern for Events.
 */
public class NullEvent extends Event {
	
	public NullEvent() {
		super.location = " is not currently in any meetings";
		super.start = new DateTime();
		super.status = "available";
		super.summary = "";
		super.description = "";
	}
	
	@Override
	public boolean isNullEvent() {
		return true;
	}

}
