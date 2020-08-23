package com.aamaro.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * Service created to handle the REST API used by the client
 * and also to communicate with the Web Scraper service and persist
 * the result in the database
 */

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
