package dev.caceresenzo.gitbook.client.impl;

import java.util.Objects;

import dev.caceresenzo.gitbook.client.GitBookClient;
import dev.caceresenzo.gitbook.client.impl.auth.AuthRequestInterceptor;
import dev.caceresenzo.gitbook.model.ApiInfo;
import dev.caceresenzo.gitbook.util.GitBookUtils;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public class GitBookClientImpl implements GitBookClient {

	private final FeignGitBookClient delegate;

	public GitBookClientImpl(
		String apiUrl,
		String accessToken
	) {
		Objects.requireNonNull(apiUrl, "apiUrl must be specified");

		final var mapper = GitBookUtils.createMapper();

		final var feignBuilder = Feign.builder()
			.encoder(new JacksonEncoder(mapper))
			.decoder(new JacksonDecoder(mapper));

		if (accessToken != null) {
			feignBuilder.requestInterceptor(new AuthRequestInterceptor(accessToken));
		}

		this.delegate = feignBuilder.target(FeignGitBookClient.class, apiUrl);
	}

	@Override
	public ApiInfo getApiInfo() {
		return delegate.getApiInfo();
	}

}