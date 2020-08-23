package com.aamaro.common.dto;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LinkDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("key")
	Long id;
	
	@JsonProperty("title")
	String url;
	String error;
	
	@JsonProperty("children")
	List<LinkDto> links;
		
	public LinkDto() {
		super();
	}
	
	public LinkDto(Long id, String url, List<LinkDto> links) {
		super();
		this.id = id;
		this.url = url;
		this.links = links;
	}	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<LinkDto> getLinks() {
		return links;
	}
	public void setLinks(List<LinkDto> links) {
		this.links = links;
	}

	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}	
}
