package com.aamaro.backend.messaging;

import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.aamaro.backend.entities.Link;
import com.aamaro.backend.repository.LinkRepository;
import com.aamaro.common.dto.LinkDto;

/*
 * Listener for the backend service, receives the response from the
 * web scraper service and persists into the database
 */

@Component
public class Listener {
		
	public static final String RESPONSE_QUEUE = "RESPONSE";
	
    @Autowired
    private LinkRepository repo;

	@JmsListener(destination = RESPONSE_QUEUE)
	public void handleMessage(LinkDto dto) throws JMSException {
		
		System.out.println("Backend Received message " + dto.toString());
		
		Link link = new Link();
		convertDtoToEntity(dto, link);
		
		if (dto.getError() == null) {
			link.setStatus("OK");
			System.out.println(link.toString());			
		}
		else {
			link.setStatus("ERROR");
			System.out.println("error");
		}
		
		//persist in database
		repo.save(link);
						
	}
	
	/*
	 * Would use some library like mapstruct to do this
	 * if the model was complex or converting more than one object
	 */
	private void convertDtoToEntity(LinkDto dto, Link entity) {
		entity.setUrl(dto.getUrl());
		List<Link> children = new ArrayList<Link>();
		
		for (LinkDto childDto: dto.getLinks()) {
			Link child = new Link();
			child.setParent(entity);
			child.setUrl(childDto.getUrl());
			convertDtoToEntity(childDto, child);
			children.add(child);
		}
		
		entity.setLinks(children);
	}
}
