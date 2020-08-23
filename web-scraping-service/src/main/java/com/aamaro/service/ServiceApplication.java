package com.aamaro.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * Web Scrapping service that receives requests from the ActiveMQ queue,
 * collects the links from the request and sends a response through
 * a response queue to the backend service
 */

@SpringBootApplication
public class ServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}
}
