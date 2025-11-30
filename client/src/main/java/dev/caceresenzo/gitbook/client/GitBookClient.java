package dev.caceresenzo.gitbook.client;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import dev.caceresenzo.gitbook.client.impl.GitBookClientImpl;
import dev.caceresenzo.gitbook.model.ApiInfo;
import dev.caceresenzo.gitbook.model.ChangeRequest;
import dev.caceresenzo.gitbook.model.File;
import dev.caceresenzo.gitbook.model.Organization;
import dev.caceresenzo.gitbook.model.Page;
import dev.caceresenzo.gitbook.model.RevisionPage;
import dev.caceresenzo.gitbook.model.Space;
import dev.caceresenzo.gitbook.model.User;
import lombok.Data;
import lombok.experimental.Accessors;

public interface GitBookClient {

	/**
	 * Get current version and build of the API.
	 */
	ApiInfo getApiInfo();

	Optional<User> findCurrentUser();

	Optional<User> findUserById(String userId);

	Stream<Organization> findAllOrganizations();

	Optional<Organization> findOrganizationById(String organizationId);

	Stream<Space> findAllSpaces(String organizationId);

	Optional<Space> findSpaceById(String spaceId);

	Optional<RevisionPage> getSpaceContent(String spaceId, String pagePath);

	Optional<List<Page>> getSpacePages(String spaceId);

	Stream<File> findAllSpaceFiles(String spaceId);

	Stream<ChangeRequest> findAllChangeRequests(String spaceId);

	Stream<ChangeRequest> findAllChangeRequests(String spaceId, ChangeRequest.Status status);

	Optional<ChangeRequest> findChangeRequestById(String spaceId, String changeRequestId);

	Optional<ChangeRequest> findChangeRequestByNumber(String spaceId, int changeRequestNumber);

	Optional<RevisionPage> getChangeRequestContent(String spaceId, String changeRequestId, String pagePath);

	Optional<List<Page>> getChangeRequestPages(String spaceId, String changeRequestId);

	Stream<File> findAllChangeRequestFiles(String spaceId, String changeRequestId);

	/**
	 * Create a new builder.
	 *
	 * @return A new {@link Builder} instance.
	 */
	static Builder builder() {
		return new Builder();
	}

	@Data
	@Accessors(fluent = true)
	public static class Builder {

		public static final String DEFAULT_API_URL = "https://api.gitbook.com";
		public static final long DEFAULT_MAX_PAGE_SIZE = 1000;

		/** The URL of the GitBook API. Defaults to `https://api.gitbook.com`. */
		private String apiUrl = DEFAULT_API_URL;

		/** The access token. */
		private String accessToken;

		/** The page size used for pagination. */
		private long maxPageSize = DEFAULT_MAX_PAGE_SIZE;

		public Builder unauthenticated() {
			return accessToken(null);
		}

		/**
		 * Build the client.
		 *
		 * @return A configured client instance.
		 */
		public GitBookClient build() {
			return new GitBookClientImpl(
				apiUrl,
				accessToken,
				maxPageSize
			);
		}

	}

}