package com.youi.finder.alexa.response;

public class InvocationResponse {
	
	private String version;
	private Response response;
	private boolean shouldEndSession = true;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
	public boolean isShouldEndSession() {
		return shouldEndSession;
	}
	public void setShouldEndSession(boolean shouldEndSession) {
		this.shouldEndSession = shouldEndSession;
	}
	@Override
	public String toString() {
		return "InvocationResponse [version=" + version + ", response=" + response + "]";
	}
}
