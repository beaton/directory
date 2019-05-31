package com.youi.finder.alexa;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.PlainTextOutputSpeech;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class AmazonFallbackIntentHandler implements IntentHandler {

	Logger logger = Logger.getLogger(AmazonFallbackIntentHandler.class);
	
	@Override
	public SpeechletResponse handleIntent(Intent intent, IntentRequest request, Session session) {

		logger.info("Handling fallback request.");
		String speechText = "I have no idea what you are saying.";

		Card card = AlexaUtils.newCard("WAT?!?!", speechText);
		PlainTextOutputSpeech speech = AlexaUtils.newSpeech(speechText, false);

		return AlexaUtils.newSpeechletResponse(card, speech, session, true);
	}
}