package com.aamaro.backend.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.aamaro.common.dto.Request;

@Component
public class Producer {
	
	/*
	 * Used to send the request to the Web Scraper service
	 * through the ActiveMQ queue
	 */

	@Autowired
	JmsTemplate jmsTemplate;

	public static final String REQUEST_QUEUE = "REQUEST";
	
	public void sendMessage(final Request request) {
		System.out.println("Sending message " + request.getUrl());		
		jmsTemplate.convertAndSend(REQUEST_QUEUE, request);
	}

}