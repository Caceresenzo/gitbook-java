package dev.caceresenzo.gitbook.client.impl;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import dev.caceresenzo.gitbook.client.GitBookClient;
import dev.caceresenzo.gitbook.client.GitBookClientException;
import dev.caceresenzo.gitbook.client.impl.auth.AuthRequestInterceptor;
import dev.caceresenzo.gitbook.client.impl.page.PageSpliterator;
import dev.caceresenzo.gitbook.model.ApiInfo;
import dev.caceresenzo.gitbook.model.ChangeRequest;
import dev.caceresenzo.gitbook.model.Organization;
import dev.caceresenzo.gitbook.model.RevisionPage;
import dev.caceresenzo.gitbook.model.Space;
import dev.caceresenzo.gitbook.model.SpaceFile;
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
			.logLevel(feign.Logger.Level.FULL)
			.logger(new feign.Logger.ErrorLogger())
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
		if (isBlank(userId)) {
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
		if (isBlank(organizationId)) {
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
		if (isBlank(organizationId)) {
			return Stream.empty();
		}

		final var firstPage = delegate.getSpaces(organizationId, maxPageSize);

		return new PageSpliterator<>(
			firstPage,
			(nextCursor) -> delegate.getSpaces(organizationId, maxPageSize, nextCursor)
		).asStream();
	}

	@Override
	public Optional<Space> findSpaceById(String spaceId) {
		if (isBlank(spaceId)) {
			return Optional.empty();
		}

		try {
			return Optional.of(delegate.getSpaceById(spaceId));
		} catch (GitBookClientException.SpaceNotFound __) {
			return Optional.empty();
		}
	}

	@Override
	public Optional<RevisionPage> getSpaceContent(String spaceId, String pagePath) {
		if (isBlank(spaceId)) {
			return Optional.empty();
		}

		if (isBlank(pagePath)) {
			pagePath = "/";
		}

		try {
			return Optional.of(delegate.getSpaceContent(spaceId, pagePath));
		} catch (GitBookClientException.SpaceNotFound | GitBookClientException.RevisionPageNotFound __) {
			return Optional.empty();
		}
	}

	@Override
	public Stream<SpaceFile> findAllSpaceFiles(String spaceId) {
		if (isBlank(spaceId)) {
			return Stream.empty();
		}

		final var firstPage = delegate.getSpaceFiles(spaceId, maxPageSize, null);

		return new PageSpliterator<>(
			firstPage,
			(nextCursor) -> delegate.getSpaceFiles(spaceId, maxPageSize, nextCursor)
		).asStream();
	}

	@Override
	public Stream<ChangeRequest> findAllChangeRequests(String spaceId) {
		return Arrays.stream(ChangeRequest.Status.values())
			.flatMap((status) -> findAllChangeRequests(spaceId, status));
	}

	@Override
	public Stream<ChangeRequest> findAllChangeRequests(String spaceId, ChangeRequest.Status status) {
		if (isBlank(spaceId)) {
			return Stream.empty();
		}

		final var firstPage = delegate.getChangeRequests(spaceId, status, maxPageSize, null);

		return new PageSpliterator<>(
			firstPage,
			(nextCursor) -> delegate.getChangeRequests(spaceId, status, maxPageSize, nextCursor)
		).asStream();
	}

	@Override
	public Optional<ChangeRequest> findChangeRequestById(String spaceId, String changeRequestId) {
		if (isBlank(spaceId) || isBlank(changeRequestId)) {
			return Optional.empty();
		}

		try {
			return Optional.of(delegate.getChangeRequestById(spaceId, changeRequestId));
		} catch (GitBookClientException.SpaceNotFound | GitBookClientException.ChangeRequestNotFound __) {
			return Optional.empty();
		}
	}

	private boolean isBlank(String value) {
		return value == null || value.isBlank();
	}

}