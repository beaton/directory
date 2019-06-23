package com.youi.finder.alexa;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.youi.finder.google.Calendar;
import com.youi.finder.google.Event;
import com.amazon.speech.slu.Slot;

/**
 * This is a custom handler that responds to the Alexa FindPeople Intent
 * (configured in the Alexa Console).
 */
@Component
public class FindPeopleHandler implements IntentHandler {

	Logger logger = Logger.getLogger(FindPeopleHandler.class);

	@Autowired
	Calendar calendar;
	
	/**
	 * Handle requests from the FindPeople Intent, defined in the Alexa Console.
	 */
	@Override
	public SpeechletResponse handleIntent(Intent intent, IntentRequest request, Session session) {

		logger.info("Handling find people Alexa intent request.");

		if (logger.isDebugEnabled()) {
			this.logSlots(intent.getSlots());
		}
		
		Slot nameSlot = intent.getSlot("name");
		String name = trimName(nameSlot.getValue());
		
		Event calendarEvent = null;
		String speechText = null;
		try {
			speechText = calendar.getCurrentLocation(name);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			speechText = "I'm having trouble connecting to Google right now, can you try again later?";
		}

		Card card = AlexaUtils.newCard("Finder finds ...", speechText);
		PlainTextOutputSpeech speech = AlexaUtils.newSpeech(speechText, false);

		return AlexaUtils.newSpeechletResponse(card, speech, session, true);
	}

	/**
	 * Convenience method for debugging that logs intent values.
	 */
	private void logSlots(Map<String, Slot> slots) {
		Collection<Slot> values = slots.values();
		Iterator<Slot> i = values.iterator();
		while (i.hasNext()) {
			Slot slot = i.next();
			logger.debug("Slot name: " + slot.getName());
			logger.debug("Slot value: " + slot.getValue());
		}
	}
	
	private String trimName(String name) {
		if (name != null) return name.trim();
		return name;
	}
}
