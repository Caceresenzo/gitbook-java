package dev.caceresenzo.gitbook.client.impl.auth;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class AuthRequestInterceptor implements RequestInterceptor {

	private final String headerValue;

	public AuthRequestInterceptor(String accessToken) {
		this.headerValue = "Bearer %s".formatted(accessToken);
	}

	@Override
	public void apply(RequestTemplate template) {
		template.header("Authorization", headerValue);
	}

}