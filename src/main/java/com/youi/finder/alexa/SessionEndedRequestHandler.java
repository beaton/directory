package com.youi.finder.alexa;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;
import static com.amazon.ask.request.Predicates.requestType;

import java.util.Optional;

import org.apache.log4j.Logger;

public class SessionEndedRequestHandler implements RequestHandler {
	
	Logger logger = Logger.getLogger(SessionEndedRequestHandler.class);

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(SessionEndedRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
    	
        //any cleanup logic goes here
        return input.getResponseBuilder().build();
    }
}
