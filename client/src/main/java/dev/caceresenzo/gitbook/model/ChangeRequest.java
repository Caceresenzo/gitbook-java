package dev.caceresenzo.gitbook.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ChangeRequest {

	@JsonProperty("id")
	private String id;

	@JsonProperty("subject")
	private String subject;

	@JsonProperty("number")
	private int number;

	@JsonProperty("status")
	private Status status;

	@JsonProperty("revision")
	private String revision;

	@JsonProperty("createdAt")
	private LocalDateTime createdAt;

	@JsonProperty("updatedAt")
	private LocalDateTime updatedAt;

	public enum Status {

		@JsonProperty("draft")
		DRAFT,

		@JsonProperty("open")
		OPEN,

		@JsonProperty("archived")
		ARCHIVED,

		@JsonProperty("merged")
		MERGED,

	}

}