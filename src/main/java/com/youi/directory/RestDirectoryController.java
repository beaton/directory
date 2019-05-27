package com.youi.directory;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
//import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping
public class RestDirectoryController {

	Logger logger = Logger.getLogger(RestDirectoryController.class);

	@RequestMapping("/greetings")
	public String index() {
		logger.info("GET index request.");
		return "Greetings from Spring Boot!";
	}
	
	/**
	 * Health monitor for I'm alive ping.
	 * Can return anything (provided it's a 200).
	 */
	@RequestMapping("/health")
	public String isAlive() {
		logger.info("GET health status.");
		return "you bet!";
	}
	
	/**
	 * Alexa JSON request to find a person.
	 */
	@RequestMapping(value = "/find", method = RequestMethod.POST)
    public ResponseEntity find(@RequestBody Map<String, Object> payload) {
		logger.info("POST find request: " + payload);
		
		return ResponseEntity.ok("success!!");
	}
}
