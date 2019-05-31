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
 * This class handles the scenario where the user tells Alexa to stop.
 */
@Component
public class AmazonStopIntentHandler implements IntentHandler {
	
	Logger logger = Logger.getLogger(AmazonStopIntentHandler.class);
	
	@Override
	public SpeechletResponse handleIntent(Intent intent, IntentRequest request, Session session) {
		
		logger.info("Handling stop request");

		String speechText= "OK. Goodbye";
		
		Card card = AlexaUtils.newCard("See ya later...", speechText);
		PlainTextOutputSpeech speech = AlexaUtils.newSpeech(speechText, false);
		
		return AlexaUtils.newSpeechletResponse(card, speech, session, true);

	}

}
