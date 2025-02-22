package dev.caceresenzo.gitbook.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Space {

	@JsonProperty("id")
	private String id;

	@JsonProperty("title")
	private String title;

	@JsonProperty("createdAt")
	private LocalDateTime createdAt;

	@JsonProperty("updatedAt")
	private LocalDateTime updatedAt;

	@JsonProperty("organization")
	private String organizationId;

	@JsonProperty("urls")
	private Urls urls;

	@Data
	public static class Urls {

		@JsonProperty("published")
		private String published;

	}

}