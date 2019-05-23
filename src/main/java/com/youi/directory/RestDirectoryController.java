package com.youi.directory;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestDirectoryController {
	
	Logger logger = Logger.getLogger(RestDirectoryController.class);

	@RequestMapping("/greetings")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
