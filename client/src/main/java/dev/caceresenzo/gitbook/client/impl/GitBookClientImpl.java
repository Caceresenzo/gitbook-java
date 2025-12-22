package dev.caceresenzo.gitbook.client.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import dev.caceresenzo.gitbook.client.GitBookClient;
import dev.caceresenzo.gitbook.client.GitBookClientException;
import dev.caceresenzo.gitbook.client.impl.auth.AuthRequestInterceptor;
import dev.caceresenzo.gitbook.client.impl.page.NextPageGetter;
import dev.caceresenzo.gitbook.client.impl.page.PageSpliterator;
import dev.caceresenzo.gitbook.model.ApiInfo;
import dev.caceresenzo.gitbook.model.ChangeRequest;
import dev.caceresenzo.gitbook.model.File;
import dev.caceresenzo.gitbook.model.Organization;
import dev.caceresenzo.gitbook.model.Page;
import dev.caceresenzo.gitbook.model.RevisionPage;
import dev.caceresenzo.gitbook.model.Site;
import dev.caceresenzo.gitbook.model.SiteStructure;
import dev.caceresenzo.gitbook.model.Space;
import dev.caceresenzo.gitbook.model.User;
import dev.caceresenzo.gitbook.util.GitBookUtils;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public class GitBookClientImpl implements GitBookClient {

	private final int maxPageSize;
	private final FeignGitBookClient delegate;

	public GitBookClientImpl(
		String apiUrl,
		String accessToken,
		int maxPageSize,
		boolean trace
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
			feignBuilder
				.requestInterceptor(new AuthRequestInterceptor(accessToken));
		}

		if (trace) {
			feignBuilder
				.logLevel(feign.Logger.Level.FULL)
				.logger(new feign.Logger.ErrorLogger());
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
		return asStream(delegate::getOrganizations);
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
	public Stream<Site> findAllSites(String organizationId) {
		if (isBlank(organizationId)) {
			return Stream.empty();
		}

		try {
			return asStream((pageSize, nextCursor) -> delegate.getSites(organizationId, pageSize, nextCursor));
		} catch (GitBookClientException.OrganizationNotFound __) {
			return Stream.empty();
		}
	}

	@Override
	public Optional<Site> findSiteById(String organizationId, String siteId) {
		if (isBlank(organizationId) || isBlank(siteId)) {
			return Optional.empty();
		}

		try {
			return Optional.of(delegate.getSiteById(organizationId, siteId));
		} catch (GitBookClientException.OrganizationNotFound | GitBookClientException.SiteNotFound __) {
			return Optional.empty();
		}
	}

	@Override
	public Optional<SiteStructure> getSiteStructure(String organizationId, String siteId) {
		if (isBlank(organizationId) || isBlank(siteId)) {
			return Optional.empty();
		}

		try {
			return Optional.of(delegate.getSiteStructure(organizationId, siteId));
		} catch (GitBookClientException.OrganizationNotFound | GitBookClientException.SiteNotFound __) {
			return Optional.empty();
		}
	}

	@Override
	public Stream<Space> findAllSpaces(String organizationId) {
		if (isBlank(organizationId)) {
			return Stream.empty();
		}

		return asStream((pageSize, nextCursor) -> delegate.getSpaces(organizationId, pageSize, nextCursor));
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
	public Optional<List<Page>> getSpacePages(String spaceId) {
		if (isBlank(spaceId)) {
			return Optional.empty();
		}

		try {
			final var response = delegate.getSpacePages(spaceId, maxPageSize, null);
			return Optional.of(response.getPages());
		} catch (GitBookClientException.SpaceNotFound __) {
			return Optional.empty();
		}
	}

	@Override
	public Stream<File> findAllSpaceFiles(String spaceId) {
		if (isBlank(spaceId)) {
			return Stream.empty();
		}

		try {
			return asStream((pageSize, nextCursor) -> delegate.getSpaceFiles(spaceId, pageSize, nextCursor));
		} catch (GitBookClientException.SpaceNotFound __) {
			return Stream.empty();
		}
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

		try {
			return asStream((pageSize, nextCursor) -> delegate.getChangeRequests(spaceId, status, pageSize, nextCursor));
		} catch (GitBookClientException.SpaceNotFound __) {
			return Stream.empty();
		}
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

	@Override
	public Optional<ChangeRequest> findChangeRequestByNumber(String spaceId, long changeRequestNumber) {
		return findChangeRequestById(spaceId, String.valueOf(changeRequestNumber));
	}

	@Override
	public Optional<RevisionPage> getChangeRequestContent(String spaceId, String changeRequestId, String pagePath) {
		if (isBlank(spaceId) || isBlank(changeRequestId)) {
			return Optional.empty();
		}

		if (isBlank(pagePath)) {
			pagePath = "/";
		}

		try {
			return Optional.of(delegate.getChangeRequestContent(spaceId, changeRequestId, pagePath));
		} catch (GitBookClientException.SpaceNotFound | GitBookClientException.ChangeRequestNotFound | GitBookClientException.RevisionPageNotFound __) {
			return Optional.empty();
		}
	}

	@Override
	public Optional<List<Page>> getChangeRequestPages(String spaceId, String changeRequestId) {
		if (isBlank(spaceId) || isBlank(changeRequestId)) {
			return Optional.empty();
		}

		try {
			final var response = delegate.getChangeRequestPages(spaceId, changeRequestId, maxPageSize, null);
			return Optional.of(response.getPages());
		} catch (GitBookClientException.SpaceNotFound | GitBookClientException.ChangeRequestNotFound __) {
			return Optional.empty();
		}
	}

	@Override
	public Stream<File> findAllChangeRequestFiles(String spaceId, String changeRequestId) {
		if (isBlank(spaceId) || isBlank(changeRequestId)) {
			return Stream.empty();
		}

		try {
			return asStream((pageSize, nextCursor) -> delegate.getChangeRequestFiles(spaceId, changeRequestId, pageSize, nextCursor));
		} catch (GitBookClientException.SpaceNotFound | GitBookClientException.ChangeRequestNotFound __) {
			return Stream.empty();
		}
	}

	private boolean isBlank(String value) {
		return value == null || value.isBlank();
	}

	private <T> Stream<T> asStream(NextPageGetter<T> nextPageGetter) {
		return PageSpliterator.of(
			maxPageSize,
			nextPageGetter
		).asStream();
	}

}