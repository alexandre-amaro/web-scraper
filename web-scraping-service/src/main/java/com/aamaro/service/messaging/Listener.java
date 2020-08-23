package com.aamaro.service.messaging;

import java.io.IOException;

import javax.jms.JMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import com.aamaro.common.dto.LinkDto;
import com.aamaro.common.dto.Request;
import com.aamaro.service.Scraper;

@Component
public class Listener {
	
	/*
	 * Listens to the request queue, collects the links and send them
	 * back to the backend service through the response queue
	 */
	
	@Autowired
	Scraper scraper;
	
	public static final String REQUEST_QUEUE = "REQUEST";
	public static final String RESPONSE_QUEUE = "RESPONSE";

	@JmsListener(destination = REQUEST_QUEUE)
	@SendTo(RESPONSE_QUEUE)
	public LinkDto handleMessage(Request request) throws JMSException {
		
		System.out.println("Service Received message " + request.getUrl());
		LinkDto linkDto = new LinkDto();
		
		try {			
			scraper.getLinks(request.getUrl(), 0, request.getMaxLevels(), linkDto);
		} catch (IOException e) {
			e.printStackTrace();
			linkDto.setUrl(request.getUrl());
			linkDto.setError(e.getMessage());
		}
		
		System.out.println("Service sent message " + linkDto.getUrl());
		return linkDto;
		
	}

}