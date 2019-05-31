package com.youi.finder.alexa;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;

import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.StandardCard;
import com.amazon.speech.ui.Reprompt;

@Service
public class HandlerSpeechlet implements SpeechletV2 {

	private Logger logger = Logger.getLogger(HandlerSpeechlet.class);
	protected static final String SESSION_CONVERSATION_FLAG = "conversation";
	public static final String SamplesHelpText = "Maybe try saying: Where is Jason";
	public static final String RepromptText = "What else can I tell you?  Say \"Help\" for some suggestions.";

	@Autowired
	private BeanFactory beanFactory;

	public HandlerSpeechlet() {
	}

	/**
	 * This is invoked when a new Alexa session is started. Any initialization logic
	 * would go here. You can store stuff in the Alexa session, for example, by
	 * calling: Session session = requestEnvelope.getSession();
	 */
	@Override
	public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
		logger.info("New session initiated");
	}

	/**
	 * This method is invoked when the user first launches the skill, for example if
	 * they say "Alexa, open you I calendar."
	 */
	@Override
	public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {

		logger.info("New conversation initiated");
		String requestId = requestEnvelope.getRequest().getRequestId();
		String sessionId = requestEnvelope.getRequest().getRequestId();
		logger.info("onLaunch requestId=" + requestId + " sessionId=" + sessionId);

		// Set a session variable so that we know we're in conversation mode.
		Session session = requestEnvelope.getSession();
		session.setAttribute(SESSION_CONVERSATION_FLAG, "true");

		// Create the initial greeting speech.
		String speechText = "Hello from You I finder. " + SamplesHelpText;

		Card card = this.newCard("Welcome!", speechText);
		PlainTextOutputSpeech speech = this.newSpeech(speechText, false);

		return this.newSpeechletResponse(card, speech, session, false);
	}

	/**
	 * This method is invoked whenever an intent is invoked by the user.
	 * 
	 * We need to figure out what the intent is, and then delegate to a handler for
	 * that specific intent.
	 */
	@Override
	public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {

		IntentRequest request = requestEnvelope.getRequest();
		Session session = requestEnvelope.getSession();

		Intent intent = request.getIntent();
		if (intent != null) {

			String intentName = intent.getName();
			String handlerBeanName = intentName + "Handler";
			logger.debug("Received intent: " + intentName);

			handlerBeanName = StringUtils.replace(handlerBeanName, "AMAZON.", "Amazon");
			handlerBeanName = handlerBeanName.substring(0, 1).toLowerCase() + handlerBeanName.substring(1);

			logger.info("About to invoke handler '" + handlerBeanName + "' for intent '" + intentName + "'.");

			try {
				Object handlerBean = beanFactory.getBean(handlerBeanName);

				if (handlerBean != null) {

					if (handlerBean instanceof IntentHandler) {
						IntentHandler intentHandler = (IntentHandler) handlerBean;
						return intentHandler.handleIntent(intent, request, session);
					}
				}
			} catch (Exception e) {
				logger.error("Error handling intent " + intentName, e);
			}
		}

		// Handle unknown intents. Ask the user for more info.
		session.setAttribute(SESSION_CONVERSATION_FLAG, "true");

		String errorText = "I'm sorry Dave, I'm afraid I can't do that. " + HandlerSpeechlet.SamplesHelpText;

		Card card = this.newCard("Dazed and Confused", errorText);
		PlainTextOutputSpeech speech = this.newSpeech(errorText, false);

		return this.newSpeechletResponse(card, speech, session, false);
	}

	@Override
	public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {

	}

	public Card newCard(String cardTitle, String cardText) {

		StandardCard card = new StandardCard();
		card.setTitle((cardTitle == null) ? "You i Finder" : cardTitle);
		card.setText(cardText);

		return card;
	}

	public PlainTextOutputSpeech newSpeech(String speechText, boolean appendRepromptText) {

		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(appendRepromptText ? speechText + "\n\n" + HandlerSpeechlet.RepromptText : speechText);

		return speech;
	}

	public SpeechletResponse newSpeechletResponse(Card card, PlainTextOutputSpeech speech, Session session,
			boolean shouldEndSession) {

		if (inConversationMode(session) && !shouldEndSession) {
			PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
			repromptSpeech.setText(HandlerSpeechlet.RepromptText);

			Reprompt reprompt = new Reprompt();
			reprompt.setOutputSpeech(repromptSpeech);

			return SpeechletResponse.newAskResponse(speech, reprompt, card);
		} else {
			return SpeechletResponse.newTellResponse(speech, card);
		}
	}

	public boolean inConversationMode(Session session) {
		return session.getAttribute(SESSION_CONVERSATION_FLAG) != null;
	}
}
