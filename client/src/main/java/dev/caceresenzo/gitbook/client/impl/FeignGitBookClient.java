package dev.caceresenzo.gitbook.client.impl;

import dev.caceresenzo.gitbook.model.ApiInfo;
import dev.caceresenzo.gitbook.model.User;
import feign.Param;
import feign.RequestLine;

public interface FeignGitBookClient {

	String JSON_CONTENT_TYPE = "Content-Type: application/json";

	@RequestLine("GET /v1/")
	ApiInfo getApiInfo();

	@RequestLine("GET /v1/user")
	User getAuthenticatedUser();

	@RequestLine("GET /v1/users/{userId}")
	User getUserById(@Param String userId);

}