package com.youi.finder.alexa.response;

public class OutputSpeech {
	
	/** PlainText */
	private String type = "PlainText";
	
	/** Plain text string to speak */
	private String text = "hello world";
	
	/**
	 * A boolean value that indicates what should happen 
	 * after Alexa speaks the response.
	 */
	private String playBehavior = "REPLACE_ENQUEUED";
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getPlayBehavior() {
		return playBehavior;
	}
	public void setPlayBehavior(String playBehavior) {
		this.playBehavior = playBehavior;
	}
	@Override
	public String toString() {
		return "OutputSpeech [type=" + type + ", text=" + text + ", playBehavior=" + playBehavior + "]";
	}

}
