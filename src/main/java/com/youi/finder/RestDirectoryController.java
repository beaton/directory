package com.youi.finder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;

import com.youi.finder.google.Calendar;
import com.youi.finder.google.Event;
import com.youi.finder.quote.Quote;

@RestController
@RequestMapping
public class RestDirectoryController {

	Logger logger = Logger.getLogger(RestDirectoryController.class);
	
	@Autowired
	Calendar googleCalendar;
	
	/**
	 * Health monitor for I'm alive ping from the Load Balancer. Will return 200
	 * HTTP response status code by default, feel free to add custom code to verify
	 * the server is healthy and where it is not, return 4xx or 5xx as appropriate.
	 */
	@RequestMapping("/health")
	public String isAlive() {
		logger.info("GET health status.");
		return "health is good!";
	}
	
	/** 
	 * Test endpoint during development (feature not currently supported).
	 * 
	 * usage: http://localhost:8080/calendar?user=andrew@foo.com
	 * 
	 * @param username user email address, andrew@foo.com for example. 
	 * @return a String message with the user's current location (meeting room).
	 */
	@RequestMapping(value = "/calendar", method = RequestMethod.GET)
	public ResponseEntity<String> calendar(@RequestParam("user") String username) {

		String speechText = "hello world";
		logger.info("Get today's meetings for: " + username);
		try {
			speechText = googleCalendar.getCurrentLocation(username);
		} catch (Exception e) {
			speechText = "Trouble getting Calendar Service for: " + username;
			logger.error(speechText, e);
		}
		return new ResponseEntity<>(speechText, HttpStatus.OK);
	}

	// Bonus section ...
	/**
	 * Sample endpoint to verify outward calls are functional. Makes a call to the
	 * gturnquist-quoters URL to get a quote about Spring boot for fun.
	 */
	@RequestMapping(value = "/quote", method = RequestMethod.GET)
	public String quote() {
		logger.info("GET quote request.");

		RestTemplate restTemplate = new RestTemplate();
		Quote quote = restTemplate.getForObject("https://gturnquist-quoters.cfapps.io/api/random", Quote.class);
		logger.info(quote.toString());
		return quote.toString();
	}
}
