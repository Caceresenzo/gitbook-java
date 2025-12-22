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
import dev.caceresenzo.gitbook.model.Site;
import dev.caceresenzo.gitbook.model.SiteStructure;
import dev.caceresenzo.gitbook.model.SiteStructure.SiteSpaces.Structure.SiteSpace;
import dev.caceresenzo.gitbook.model.Space;
import dev.caceresenzo.gitbook.model.User;
import lombok.Data;
import lombok.experimental.Accessors;

public interface GitBookClient {

	/**
	 * Access the release version and build date of the GitBook codebase.
	 *
	 * @return The {@link ApiInfo API information}.
	 */
	ApiInfo getApiInfo();

	/**
	 * Get profile of authenticated user.
	 *
	 * @return The authenticated user, if authenticated.
	 */
	Optional<User> findCurrentUser();

	/**
	 * Get a user by its ID.
	 *
	 * @param userId The unique ID of the {@link User}.
	 * @return A {@link User user}, if it exists.
	 */
	Optional<User> findUserById(String userId);

	/**
	 * Get the list of organizations for the currently authenticated {@link User user}.
	 *
	 * @return A {@link Stream stream} of {@link Organization organizations}.
	 */
	Stream<Organization> findAllOrganizations();

	/**
	 * Get an organization by its ID.
	 *
	 * @param organizationId The unique ID of the {@link Organization organization}.
	 * @return An {@link Organization organization}, if it exists.
	 */
	Optional<Organization> findOrganizationById(String organizationId);

	Stream<Site> findAllSites(String organizationId);

	Optional<Site> findSiteById(String organizationId, String siteId);

	Optional<SiteStructure> getSiteStructure(String organizationId, String siteId);

	/**
	 * List all spaces.
	 *
	 * @param organizationId The unique ID of the organization.
	 * @return A {@link Stream stream} of {@link SiteSpace spaces} for the specified {@link Organization organization}.
	 */
	Stream<Space> findAllSpaces(String organizationId);

	/**
	 * Get a space by its ID.
	 *
	 * @param spaceId The unique ID of the space.
	 * @return A {@link SiteSpace space}, if it exists.
	 */
	Optional<Space> findSpaceById(String spaceId);

	/**
	 * Get a space page by its path.
	 *
	 * @param spaceId The unique ID of the space.
	 * @param pagePath The path of the page in the revision.
	 * @return An {@link RevisionPage revision page}, if it exists.
	 * @implSpec The page path will be URL encoded, so it can contain slashes (<code>/</code>) and other special characters.
	 * @implSpec If the space does not exist, {@link Optional#empty()} is returned.
	 */
	Optional<RevisionPage> getSpaceContent(String spaceId, String pagePath);

	/**
	 * List all space pages.
	 *
	 * @param spaceId The unique ID of the space.
	 * @return A {@link List list} of {@link Page pages} in the {@link SiteSpace space}.
	 * @implSpec If the space does not exist, {@link Optional#empty()} is returned.
	 */
	Optional<List<Page>> getSpacePages(String spaceId);

	/**
	 * List all space files.
	 *
	 * @param spaceId The unique ID of the space.
	 * @return A {@link Stream stream} of {@link File files} in the {@link SiteSpace space}.
	 * @implSpec If the space does not exist, {@link Stream#empty()} is returned.
	 */
	Stream<File> findAllSpaceFiles(String spaceId);

	/**
	 * List all change requests for each statuses in the enum order.
	 *
	 * @param spaceId The unique ID of the space.
	 * @return A {@link Stream stream} of {@link ChangeRequest change requests} in the {@link SiteSpace space}.
	 * @see ChangeRequest.Status
	 * @see GitBookClient#findAllChangeRequests(String, ChangeRequest.Status)
	 * @implSpec If the space does not exist, {@link Stream#empty()} is returned.
	 */
	Stream<ChangeRequest> findAllChangeRequests(String spaceId);

	/**
	 * List all change requests.
	 *
	 * @param spaceId The unique ID of the space.
	 * @param status Only change requests matching this {@link ChangeRequest.Status status} will be returned.
	 * @return A {@link Stream stream} of {@link ChangeRequest change requests} in the {@link SiteSpace space}.
	 * @see GitBookClient#findAllChangeRequests(String)
	 * @implSpec If the space does not exist, {@link Stream#empty()} is returned.
	 */
	Stream<ChangeRequest> findAllChangeRequests(String spaceId, ChangeRequest.Status status);

	/**
	 * Get a change request by its ID.
	 *
	 * @param spaceId The unique ID of the space.
	 * @param changeRequestId The unique ID of the change request.
	 * @return An {@link ChangeRequest change request}, if it exists.
	 * @see GitBookClient#findChangeRequestByNumber(String, int)
	 * @implSpec If the space does not exist, {@link Optional#empty()} is returned.
	 */
	Optional<ChangeRequest> findChangeRequestById(String spaceId, String changeRequestId);

	/**
	 * Get a change request by its ID.
	 *
	 * @param spaceId The unique ID of the space.
	 * @param changeRequestNumber The change request {@link ChangeRequest#getNumber() number identifier} in the space.
	 * @return An {@link ChangeRequest change request}, if it exists.
	 * @see GitBookClient#findChangeRequestById(String, String)
	 * @implSpec If the space does not exist, {@link Optional#empty()} is returned.
	 */
	Optional<ChangeRequest> findChangeRequestByNumber(String spaceId, long changeRequestNumber);

	/**
	 * Get a change request page by its path.
	 *
	 * @param spaceId The unique ID of the space.
	 * @param changeRequestId The unique ID of the change request.
	 * @param pagePath The path of the page in the revision.
	 * @return An {@link RevisionPage revision page}, if it exists.
	 * @implSpec The page path will be URL encoded, so it can contain slashes (<code>/</code>) and other special characters.
	 * @implSpec If the space or the change request does not exist, {@link Optional#empty()} is returned.
	 */
	Optional<RevisionPage> getChangeRequestContent(String spaceId, String changeRequestId, String pagePath);

	/**
	 * List all change request pages.
	 *
	 * @param spaceId The unique ID of the space.
	 * @param changeRequestId The unique ID of the change request.
	 * @return A {@link List list} of {@link Page pages} in the change request.
	 * @implSpec If the space or the change request does not exist, {@link Optional#empty()} is returned.
	 */
	Optional<List<Page>> getChangeRequestPages(String spaceId, String changeRequestId);

	/**
	 * List all change request files.
	 *
	 * @param spaceId The unique ID of the space.
	 * @param changeRequestId The unique ID of the change request.
	 * @return A {@link Stream stream} of {@link File files} in the change request.
	 * @implSpec If the space or the change request does not exist, {@link Stream#empty()} is returned.
	 */
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
		public static final int DEFAULT_MAX_PAGE_SIZE = 1000;

		/** The URL of the GitBook API. Defaults to `https://api.gitbook.com`. */
		private String apiUrl = DEFAULT_API_URL;

		/** The access token. */
		private String accessToken;

		/** The page size used for pagination. */
		private int maxPageSize = DEFAULT_MAX_PAGE_SIZE;

		/** Print HTTP exchanges in {@link System#err}. Can leak the Access Token! */
		private boolean trace = false;

		/**
		 * Use no access token.
		 *
		 * @return <code>this</code>.
		 */
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
				maxPageSize,
				trace
			);
		}

	}

}