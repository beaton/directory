package com.youi.finder.alexa.request;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Request {

	private String type;
	private String requestId;
	private String timestamp;
	private Locale locale;
	private Intent intent;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public Intent getIntent() {
		return intent;
	}
	public void setIntent(Intent intent) {
		this.intent = intent;
	}
	@Override
	public String toString() {
		return "Request [type=" + type + ", requestId=" + requestId + ", timestamp=" + timestamp + ", locale=" + locale
				+ ", intent=" + intent + "]";
	}
	
}
