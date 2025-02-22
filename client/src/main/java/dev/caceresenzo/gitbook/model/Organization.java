package dev.caceresenzo.gitbook.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Organization {

	@JsonProperty("id")
	private String id;

	@JsonProperty("title")
	private String title;
	
	@JsonProperty("createdAt")
	private LocalDateTime createdAt;

}