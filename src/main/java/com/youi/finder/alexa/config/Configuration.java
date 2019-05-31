package com.youi.finder.alexa.config;

import javax.servlet.Servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;
import com.youi.finder.alexa.HandlerSpeechlet;

@org.springframework.context.annotation.Configuration
public class Configuration {

	@Autowired
	private HandlerSpeechlet handlerSpeechlet;
	
	@Bean
	public ServletRegistrationBean<Servlet> registerSpeechletServlet() {
		
		SpeechletServlet speechletServlet = new SpeechletServlet();
		speechletServlet.setSpeechlet(handlerSpeechlet);
		
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(speechletServlet, "/find");
		return servletRegistrationBean;
	}
	
}
