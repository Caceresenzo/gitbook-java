package dev.caceresenzo.gitbook.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class User {

	@JsonProperty("id")
	private String id;

	@JsonProperty("displayName")
	private String displayName;

	@JsonProperty("email")
	private String email;

	@JsonProperty("photoURL")
	private String photoUrl;

}