package com.aamaro.common.dto;

import java.time.LocalDateTime;

public interface LinkProjection {
	Long getId();
	String getUrl();
	String getStatus();
	LocalDateTime getCreatedOn();
}
