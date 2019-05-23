package com.youi.directory;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * This is essentially a test Controller to verify Spring Boot is up and running.
 */
@Controller
public class HelloWorldController {

	Logger logger = Logger.getLogger(HelloWorldController.class);
	
	/**
	 * Test variable configured in the application.properties to verify jslt is running.
	 */
	@Value("${application.message:Hello World}")
	private String message = "Hello there!";
	
	/**
	 * A GET request to the server without a path will redirect to the hello.jsp
	 */
	@GetMapping("/")
	String home(Map<String, Object> model) {
		logger.info("GET Hello World request.");
		model.put("message", this.message);
		return "hello";
	}
	
	/**
	 * A GET request to the server with the path /snoop will redirect to the snoop.jsp
	 */
	@GetMapping("/snoop")
    public String welcome(Map<String, Object> model) {
		logger.info("GET snoop JSP request.");
		model.put("time", new Date());
		model.put("message", this.message);
        return "snoop";
    }

}
