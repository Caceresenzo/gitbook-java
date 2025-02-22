package dev.caceresenzo.gitbook.client.impl;

import dev.caceresenzo.gitbook.model.ApiInfo;
import feign.RequestLine;

public interface FeignGitBookClient {

	String JSON_CONTENT_TYPE = "Content-Type: application/json";

	@RequestLine("GET /v1/")
	ApiInfo getApiInfo();

}