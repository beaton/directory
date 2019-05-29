package com.youi.finder;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youi.finder.alexa.request.InvocationRequest;
import com.youi.finder.alexa.request.Quote;
import com.youi.finder.alexa.response.InvocationResponse;

import org.springframework.web.bind.annotation.*;
//import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping
public class RestDirectoryController {

	Logger logger = Logger.getLogger(RestDirectoryController.class);

	// @Autowired
	// protected Foo request;

	@RequestMapping("/hello")
	public String index() {
		logger.info("GET index request.");
		return "HEY!";
	}

	/**
	 * Alexa JSON request to get usage data from Google Calendar Service. This
	 * method calls the Google calendar usage service to return meeting room usage
	 * detail.
	 */
	@RequestMapping(value = "/getUsageData", method = RequestMethod.POST)
	public ResponseEntity getUsageData(@RequestBody InvocationRequest aRequest) {
		logger.info(aRequest.toString());
		// TODO: call something autowired to process the request.

		ObjectMapper mapper = new ObjectMapper();

		InvocationResponse response = new InvocationResponse();

		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(response);
			if (logger.isDebugEnabled()) {
				logger.debug(response.toString());
			}
		} catch (JsonProcessingException ex) {
			logger.error("Error parsing response object", ex);
			return ResponseEntity.ok("bad request");
		}
		return ResponseEntity.ok(jsonString);
	}

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

	// Bonus section ...

	/**
	 * Test method to simply dump out the POST body to the logs.
	 */
	@RequestMapping(value = "/foo", method = RequestMethod.POST)
	public ResponseEntity find(@RequestBody Map<String, Object> payload) {
		logger.info("POST find request: " + payload);

		ObjectMapper mapper = new ObjectMapper();

		InvocationResponse response = new InvocationResponse();

		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(response);
			if (logger.isDebugEnabled()) {
				logger.debug(response.toString());
			}
		} catch (JsonProcessingException ex) {
			logger.error("Error parsing response object", ex);
			return ResponseEntity.ok("bad request");
		}
		return ResponseEntity.ok(jsonString);

		// return ResponseEntity.ok("success!!");
	}

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
