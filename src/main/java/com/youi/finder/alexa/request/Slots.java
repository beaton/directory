package com.youi.finder.alexa.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Slots {
	
	private Name name;
	
	public Slots() {
		name = new Name();
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Slot [name=" + name + "]";
	}

}
