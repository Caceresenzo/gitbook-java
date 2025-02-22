package dev.caceresenzo.gitbook.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ApiInfo {

	@JsonProperty("version")
	private String version;

	@JsonProperty("build")
	private LocalDateTime build;

}