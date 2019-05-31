package com.youi.finder.alexa;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.PlainTextOutputSpeech;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * This class handles the scenario where the user tells Alexa to cancel.
 */
@Component
public class AmazonCancelIntentHandler implements IntentHandler {

	Logger logger = Logger.getLogger(AmazonCancelIntentHandler.class);
			
	@Override
	public SpeechletResponse handleIntent(Intent intent, IntentRequest request, Session session) {

		logger.info("Handling cancel request");
		String speechText = "OK. fine. Maybe don't ask next time.";

		Card card = AlexaUtils.newCard("See ya later...", speechText);
		PlainTextOutputSpeech speech = AlexaUtils.newSpeech(speechText, false);

		return AlexaUtils.newSpeechletResponse(card, speech, session, true);
	}
}
