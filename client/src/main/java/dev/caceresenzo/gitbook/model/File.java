package dev.caceresenzo.gitbook.model;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class File {

	@JsonProperty("id")
	private String id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("contentType")
	private String contentType;

	@JsonProperty("downloadURL")
	private URI downloadUrl;

	@JsonProperty("size")
	private long size;

	@JsonProperty("dimensions")
	private Dimensions dimensions;

	@Data
	public static class Dimensions {

		@JsonProperty("width")
		private int width;

		@JsonProperty("height")
		private int height;

	}

}