package com.youi.finder.alexa;

import org.apache.log4j.Logger;

import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.StandardCard;

/**
 * Commonly used behaviors that belong in an abstact class and not a utility class! 
 * 
 * TODO: refactor to use an abstract class for handlers.
 */
public class AlexaUtils {

	protected static Logger logger = Logger.getLogger(AlexaUtils.class);

	protected static final String SESSION_CONVERSATION_FLAG = "conversation";

	public static final String SamplesHelpText = "Try saying: Where is Ken";
	public static final String RepromptText = "What else can I tell you?  Say \"Help\" for some suggestions.";

	public static Card newCard(String cardTitle, String cardText) {

		logger.info("Create a new card.");
		StandardCard card = new StandardCard();
		card.setTitle((cardTitle == null) ? "Youi Calendar" : cardTitle);
		card.setText(cardText);

		/*
		 * Image cardImage = new Image(); cardImage.setSmallImageUrl(
		 * "https://www.cutlerstew.com/static/images/cutlerstew-720x480.png");
		 * cardImage.setLargeImageUrl(
		 * "https://www.cutlerstew.com/static/images/cutlerstew-1200x800.png");
		 * 
		 * card.setImage(cardImage);
		 */

		return card;
	}

	public static PlainTextOutputSpeech newSpeech(String speechText, boolean appendRepromptText) {

		logger.info("Create new speech: " + speechText);
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(appendRepromptText ? speechText + "\n\n" + AlexaUtils.RepromptText : speechText);

		return speech;
	}

	/**
	 * Send the response to the user.
	 */
	public static SpeechletResponse newSpeechletResponse(Card card, PlainTextOutputSpeech speech, Session session,
			boolean shouldEndSession) {

		logger.info("Responding to user.");
		if (AlexaUtils.inConversationMode(session) && !shouldEndSession) {
			PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
			repromptSpeech.setText(AlexaUtils.RepromptText);

			Reprompt reprompt = new Reprompt();
			reprompt.setOutputSpeech(repromptSpeech);

			return SpeechletResponse.newAskResponse(speech, reprompt, card);
		} else {
			return SpeechletResponse.newTellResponse(speech, card);
		}
	}

	public static void setConversationMode(Session session, boolean conversationMode) {
		if (conversationMode)
			session.setAttribute(SESSION_CONVERSATION_FLAG, "true");
		else
			session.removeAttribute(SESSION_CONVERSATION_FLAG);
	}

	/**
	 * Return a boolean to indicate conversational state.
	 */
	public static boolean inConversationMode(Session session) {
		return session.getAttribute(SESSION_CONVERSATION_FLAG) != null;
	}
}
