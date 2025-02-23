package dev.caceresenzo.gitbook.client.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import dev.caceresenzo.gitbook.client.GitBookClient;
import dev.caceresenzo.gitbook.client.GitBookClientException;
import dev.caceresenzo.gitbook.client.impl.auth.AuthRequestInterceptor;
import dev.caceresenzo.gitbook.client.impl.page.PageSpliterator;
import dev.caceresenzo.gitbook.model.ApiInfo;
import dev.caceresenzo.gitbook.model.Organization;
import dev.caceresenzo.gitbook.model.RevisionPage;
import dev.caceresenzo.gitbook.model.Space;
import dev.caceresenzo.gitbook.model.User;
import dev.caceresenzo.gitbook.util.GitBookUtils;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public class GitBookClientImpl implements GitBookClient {

	private final long maxPageSize;
	private final FeignGitBookClient delegate;

	public GitBookClientImpl(
		String apiUrl,
		String accessToken,
		long maxPageSize
	) {
		Objects.requireNonNull(apiUrl, "apiUrl must be specified");

		if (maxPageSize < 1) {
			throw new IllegalArgumentException("maxPageSize must be positive");
		}

		final var mapper = GitBookUtils.createMapper();

		final var feignBuilder = Feign.builder()
			.encoder(new JacksonEncoder(mapper))
			.decoder(new JacksonDecoder(mapper))
			.errorDecoder(new FeignGitBookErrorDecoder(mapper));

		if (accessToken != null) {
			feignBuilder.requestInterceptor(new AuthRequestInterceptor(accessToken));
		}

		this.maxPageSize = maxPageSize;
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
		final var firstPage = delegate.getOrganizations(maxPageSize);

		return new PageSpliterator<>(
			firstPage,
			(nextCursor) -> delegate.getOrganizations(maxPageSize, nextCursor)
		).asStream();
	}

	@Override
	public Optional<Organization> findOrganizationById(String organizationId) {
		if (organizationId == null) {
			return Optional.empty();
		}

		try {
			return Optional.of(delegate.getOrganizationById(organizationId));
		} catch (GitBookClientException.OrganizationNotFound __) {
			return Optional.empty();
		}
	}

	@Override
	public Stream<Space> findAllSpaces(String organizationId) {
		final var firstPage = delegate.getSpaces(organizationId, maxPageSize);

		return new PageSpliterator<>(
			firstPage,
			(nextCursor) -> delegate.getSpaces(organizationId, maxPageSize, nextCursor)
		).asStream();
	}

	@Override
	public Optional<Space> findSpaceById(String spaceId) {
		if (spaceId == null) {
			return Optional.empty();
		}

		try {
			return Optional.of(delegate.getSpaceById(spaceId));
		} catch (GitBookClientException.SpaceNotFound __) {
			return Optional.empty();
		}
	}

	@Override
	public RevisionPage getSpaceContent(String spaceId, String pagePath) {
		return delegate.getSpaceContent(spaceId, pagePath);
	}

}