package com.aamaro.common.dto;

import java.io.Serializable;

public class Request implements Serializable {
	String url;
	int maxLevels;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	private static final long serialVersionUID = 2L;
	
	public int getMaxLevels() {
		return maxLevels;
	}
	public void setMaxLevels(int maxLevels) {
		this.maxLevels = maxLevels;
	}
	public Request(String url, int maxLevels) {
		super();
		this.url = url;
		this.maxLevels = maxLevels;
	}

}