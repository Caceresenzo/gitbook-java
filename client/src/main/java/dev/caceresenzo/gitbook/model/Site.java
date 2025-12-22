package dev.caceresenzo.gitbook.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Site {

	@JsonProperty("id")
	private String id;

	@JsonProperty("type")
	private String type;

	@JsonProperty("title")
	private String title;

	@JsonProperty("hostname")
	private String hostname;

	@JsonProperty("basename")
	private String basename;

	@JsonProperty("published")
	private boolean published;

	@JsonProperty("siteSpaces")
	private int siteSpaces;

	@JsonProperty("createdAt")
	private LocalDateTime createdAt;

	@JsonProperty("urls")
	private Urls urls;

	@Data
	public static class Urls {

		@JsonProperty("location")
		private String location;

		@JsonProperty("app")
		private String application;

		@JsonProperty("published")
		private String published;

	}

}