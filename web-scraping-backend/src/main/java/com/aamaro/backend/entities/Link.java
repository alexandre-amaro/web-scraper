package com.aamaro.backend.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Link {
	@Id
	@GeneratedValue
	Long id;
	
    @ManyToOne
	Link parent;
    
    @CreationTimestamp
    LocalDateTime createdOn; 
    
    String status;
	
    @Column(columnDefinition = "TEXT")
    String url;
    
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="parent")
	List<Link> links;
	
	public Link getParent() {
		return parent;
	}

	public void setParent(Link parent) {
		this.parent = parent;
	}	
		
	public Link() {
		super();
	}
	
	public Link(Long id, String url, List<Link> links) {
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
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
