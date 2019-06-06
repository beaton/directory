package com.youi.finder;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.youi.finder.calendar.Calendar;
import com.youi.finder.quote.Quote;

@RestController
@RequestMapping
public class RestDirectoryController {

	Logger logger = Logger.getLogger(RestDirectoryController.class);

	/**
	 * Health monitor for I'm alive ping from the Load Balancer. Will return 200
	 * HTTP response status code by default, feel free to add custom code to verify
	 * the server is healthy and where it is not, return 4xx or 5xx as appropriate.
	 */
	@RequestMapping("/health")
	public String isAlive() {
		logger.info("GET health status.");
		return "you bet!";
	}
	
	// Backdoor for testing purposes
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public String test() {
		
		Calendar calendar = new Calendar();
		String response = calendar.getMeetingRoom("ken");
		
		return response;
	
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
