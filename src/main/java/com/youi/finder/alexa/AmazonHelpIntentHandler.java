package com.youi.finder.alexa;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.PlainTextOutputSpeech;

/**
 * This class handles the scenario where the user asks Alexa for help.
 */
@Component
public class AmazonHelpIntentHandler implements IntentHandler {
	
	Logger logger = Logger.getLogger(AmazonHelpIntentHandler.class);
			
	@Override
	public SpeechletResponse handleIntent(Intent intent, IntentRequest request, Session session) {

		Card card = AlexaUtils.newCard("Help", AlexaUtils.SamplesHelpText);
		PlainTextOutputSpeech speech = AlexaUtils.newSpeech(AlexaUtils.SamplesHelpText, false);

		return AlexaUtils.newSpeechletResponse(card, speech, session, false);
	}
}
