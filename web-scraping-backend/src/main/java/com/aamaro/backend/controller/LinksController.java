package com.aamaro.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aamaro.backend.entities.Link;
import com.aamaro.backend.messaging.Producer;
import com.aamaro.backend.repository.LinkRepository;
import com.aamaro.common.dto.LinkDto;
import com.aamaro.common.dto.LinkProjection;
import com.aamaro.common.dto.Request;

/*
 * REST API 
 */

@RestController
public class LinksController {

	//injects the ActiveMQ producer
    @Autowired
    Producer producer;
    
    //injects the JPA Repository
    @Autowired
    private LinkRepository repo;    
    
    
    /*
     * Sends the request to the queue to be processed by the
     * Web Scrapper service asynchronously
     */
    @GetMapping("/api/request")
    ResponseEntity<String> getUrl(@RequestParam String url, @RequestParam Integer maxLevels){
    	
    	//input validation
    	if (maxLevels == null || maxLevels == 0 || maxLevels > 3) {
    		return ResponseEntity.badRequest().body("Invalid number of levels");
    	}

    	if (url == null || !(url.startsWith("http://") ||  url.startsWith("https://") || url.startsWith("www."))) {
    		return ResponseEntity.badRequest().body("Invalid URL");
    	}
    	
    	//remove previous execution from the database
    	repo.deleteByUrl(url);
    	
    	//send a message to the service thru the AMQ queue
    	producer.sendMessage(new Request(url, maxLevels));
    	
    	return ResponseEntity.ok(null);
    }
    
    /*
     * This endpoint is invoked by the frontend every 5 seconds when
     * there is a request in progress to the if the URL processing has
     * been completed. If the request is completed
     */
    @GetMapping("/api/poll")
    ResponseEntity<List<LinkProjection>> poll(@RequestParam List<String> urls) {
    	List<LinkProjection> links = repo.poll(urls);
    	return ResponseEntity.ok(links);
    }
    
    /*
     * List all the URLs (only where parent is null) to be
     * displayed in the UI table
     */
    @GetMapping("/api/list")
    ResponseEntity<List<LinkProjection>> list() {
    	List<LinkProjection> links = repo.findAllProjectedBy();
    	return ResponseEntity.ok(links);
    }
    
    /*
     * Retrieves the complete hierarchy of links for the
     * parent URL to be displayed on the treeview
     */
    @RequestMapping("/api/retrieve/{id}")
    ResponseEntity<LinkDto> retrieve(@PathVariable(value="id") Long id) {
    	Link link = repo.findById(id).orElse(null);
    	
    	if (link != null) {
    		LinkDto dto = new LinkDto();
    		convertEntityToDto(link, dto);
    		return ResponseEntity.ok(dto);
    	}
    	else {
    		return ResponseEntity.notFound().build();
    	}
    }
    
	/*
	 * Would use some library like mapstruct to do this
	 * if the model was complex or converting more than one object
	 */
	private void convertEntityToDto(Link entity, LinkDto dto) {
		dto.setUrl(entity.getUrl());
		dto.setId(entity.getId());
		List<LinkDto> children = new ArrayList<LinkDto>();
		
		for (Link childEntity: entity.getLinks()) {
			LinkDto child = new LinkDto();
			child.setUrl(childEntity.getUrl());
			convertEntityToDto(childEntity, child);
			children.add(child);
		}
		
		dto.setLinks(children);
	}    
    
}
