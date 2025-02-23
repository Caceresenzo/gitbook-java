package dev.caceresenzo.gitbook.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.caceresenzo.gitbook.model.document.Document;
import lombok.Data;

@Data
public class RevisionPage {

	@JsonProperty("id")
	private String id;

	@JsonProperty("title")
	private String title;

	@JsonProperty("description")
	private String description;

	@JsonProperty("path")
	private String path;

	@JsonProperty("slug")
	private String slug;

	@JsonProperty("createdAt")
	private LocalDateTime createdAt;

	@JsonProperty("updatedAt")
	private LocalDateTime updatedAt;

	@JsonProperty("document")
	private Document document;

}