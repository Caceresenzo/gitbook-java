package dev.caceresenzo.gitbook.client.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import dev.caceresenzo.gitbook.client.GitBookClient;
import dev.caceresenzo.gitbook.client.GitBookClientException;
import dev.caceresenzo.gitbook.client.impl.auth.AuthRequestInterceptor;
import dev.caceresenzo.gitbook.model.ApiInfo;
import dev.caceresenzo.gitbook.model.Organization;
import dev.caceresenzo.gitbook.model.User;
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
			.decoder(new JacksonDecoder(mapper))
			.errorDecoder(new FeignGitBookErrorDecoder(mapper));

		if (accessToken != null) {
			feignBuilder.requestInterceptor(new AuthRequestInterceptor(accessToken));
		}

		this.delegate = feignBuilder.target(FeignGitBookClient.class, apiUrl);
	}

	@Override
	public ApiInfo getApiInfo() {
		return delegate.getApiInfo();
	}

	@Override
	public Optional<User> findCurrentUser() {
		try {
			return Optional.of(delegate.getAuthenticatedUser());
		} catch (GitBookClientException.AuthenticationRequired | GitBookClientException.InvalidAuthenticationToken __) {
			return Optional.empty();
		}
	}

	@Override
	public Optional<User> findUserById(String userId) {
		if (userId == null) {
			return Optional.empty();
		}

		try {
			return Optional.of(delegate.getUserById(userId));
		} catch (GitBookClientException.UserNotFound __) {
			return Optional.empty();
		}
	}
	
	@Override
	public Stream<Organization> findAllOrganizations() {
		return delegate.getOrganizations().stream();
	}
	
	@Override
	public Optional<Organization> findOrganizationById(String organizationId) {
		return Optional.of(delegate.getOrganizationById(organizationId));
	}

}