package com.youi.finder.alexa.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Intent {
	
	private String name;
	private String confirmationStatus = "NONE";
	private Slots slots;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConfirmationStatus() {
		return confirmationStatus;
	}

	public void setConfirmationStatus(String confirmationStatus) {
		this.confirmationStatus = confirmationStatus;
	}

	public Slots getSlot() {
		return slots;
	}

	public void setSlot(Slots slots) {
		this.slots = slots;
	}

	@Override
	public String toString() {
		return "Intent [name=" + name + ", confirmationStatus=" + confirmationStatus + ", slots=" + slots + "]";
	}
}
