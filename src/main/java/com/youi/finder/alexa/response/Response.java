package com.youi.finder.alexa.response;

public class Response {
	
	private OutputSpeech outputSpeech;

	public OutputSpeech getOutputSpeech() {
		return outputSpeech;
	}

	public void setOutputSpeech(OutputSpeech outputSpeech) {
		this.outputSpeech = outputSpeech;
	}

	@Override
	public String toString() {
		return "Response [outputSpeech=" + outputSpeech + "]";
	}

}
