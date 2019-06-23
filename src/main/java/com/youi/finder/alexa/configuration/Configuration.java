package com.youi.finder.alexa.configuration;

import javax.servlet.Servlet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;
import com.youi.finder.alexa.HandlerSpeechlet;

/**
 * Registers the Alexa SDK servlet that listens to /find endpoint.
 * Note, this is witchcraft for Alexa to run in Spring Boot, very cool.
 * The Spring Boot configuration annotation registers this class, which then loads
 * the Alexa SDK SpeechletServlet to listen for POST requests on /find path.
 */
@org.springframework.context.annotation.Configuration
public class Configuration {
	
	protected static Logger logger = Logger.getLogger(Configuration.class);

	@Autowired
	private HandlerSpeechlet handlerSpeechlet;
	
	@Bean
	public ServletRegistrationBean<Servlet> registerSpeechletServlet() {
		
		logger.info("Register the Speechlet Servlet to respond to /find POST requests.");
		
		SpeechletServlet speechletServlet = new SpeechletServlet();
		speechletServlet.setSpeechlet(handlerSpeechlet);
		
		@SuppressWarnings("unchecked")
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(speechletServlet, "/find");
		return servletRegistrationBean;
	}
	
}
