package com.youi.finder.alexa.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Request {

	private String type;
	private String requestId;
	private String timestamp;
	private String locale;
	private boolean shouldLinkResultBeReturned;
	
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
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public boolean isShouldLinkResultBeReturned() {
		return shouldLinkResultBeReturned;
	}
	public void setShouldLinkResultBeReturned(boolean shouldLinkResultBeReturned) {
		this.shouldLinkResultBeReturned = shouldLinkResultBeReturned;
	}
	@Override
	public String toString() {
		return "Request [type=" + type + ", requestId=" + requestId + ", timestamp=" + timestamp + ", locale=" + locale
				+ ", shouldLinkResultBeReturned=" + shouldLinkResultBeReturned + "]";
	}
}
