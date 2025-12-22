package dev.caceresenzo.gitbook.client.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import dev.caceresenzo.gitbook.BaseGitBookTest;
import dev.caceresenzo.gitbook.client.GitBookClient;
import dev.caceresenzo.gitbook.client.GitBookClientException;
import dev.caceresenzo.gitbook.model.ChangeRequest;

class GitBookClientImplTest extends BaseGitBookTest {

	static GitBookClient unauthenticatedClient;

	public static final String ORGANIZATION_ID_ENV_VAR = "GITBOOK_COMPONENTS_ORGANIZATION_ID";
	public static final String SITE_ID_ENV_VAR = "GITBOOK_COMPONENTS_SITE_ID";
	public static final String SPACE_ID_ENV_VAR = "GITBOOK_COMPONENTS_SPACE_ID";
	public static final String CHANGE_REQUEST_ID_ENV_VAR = "GITBOOK_COMPONENTS_CHANGE_REQUEST_ID";

	static String organizationId;
	static String siteId;
	static String spaceId;
	static String changeRequestId;

	@BeforeAll
	static void setUp() {
		organizationId = assertEnvironmentVariable(ORGANIZATION_ID_ENV_VAR);
		siteId = assertEnvironmentVariable(SITE_ID_ENV_VAR);
		spaceId = assertEnvironmentVariable(SPACE_ID_ENV_VAR);
		changeRequestId = assertEnvironmentVariable(CHANGE_REQUEST_ID_ENV_VAR);

		unauthenticatedClient = GitBookClient.builder()
			.unauthenticated()
			.build();
	}

	@Test
	void getApiInfo() {
		final var apiInfo = unauthenticatedClient.getApiInfo();

		assertNotNull(apiInfo);
		assertNotNull(apiInfo.getVersion());
		assertNotNull(apiInfo.getBuild());
	}

	@Test
	void findCurrentUser() {
		final var currentUser = client.findCurrentUser();

		assertThat(currentUser).isNotEmpty();
	}

	@Test
	void findCurrentUserWhenUnauthenticated() {
		final var currentUser = unauthenticatedClient.findCurrentUser();

		assertThat(currentUser).isEmpty();
	}

	@Test
	void findUserById() {
		final var currentUser = client.findCurrentUser();
		final var user = client.findUserById(currentUser.get().getId());

		assertEquals(currentUser.get(), user.get());
	}

	@Test
	void findAllOrganizations() {
		final var organizations = client.findAllOrganizations()
			.limit(1)
			.toList();

		assertThat(organizations).hasSizeGreaterThanOrEqualTo(1);
	}

	@Test
	void findAllOrganizationsWhenUnauthenticated() {
		assertThrows(GitBookClientException.AuthenticationRequired.class, unauthenticatedClient::findAllOrganizations);
	}

	@Test
	void findOrganizationById() {
		final var organization = client.findOrganizationById(organizationId);

		assertThat(organization).isNotEmpty();
	}

	@Test
	void findOrganizationByIdWhenUnauthenticated() {
		final var organization = unauthenticatedClient.findOrganizationById(organizationId);

		assertThat(organization).isNotEmpty();
	}

	@Test
	void findOrganizationByIdWhenNotFound() {
		final var organization = unauthenticatedClient.findOrganizationById("x");

		assertThat(organization).isEmpty();
	}

	@Test
	void findAllSites() {
		final var sites = client.findAllSites(organizationId)
			.limit(1)
			.toList();

		assertThat(sites).hasSizeGreaterThanOrEqualTo(1);
	}

	@Test
	void findSiteById() {
		final var site = client.findSiteById(organizationId, siteId);

		assertThat(site).isNotEmpty();
	}

	@Test
	void findSiteByIdWhenUnauthenticated() {
		assertThrows(GitBookClientException.AuthenticationRequired.class, () -> {
			unauthenticatedClient.findSiteById(organizationId, siteId);
		});

	}

	@Test
	void findSiteByIdWhenNotFound() {
		final var site = client.findSiteById(organizationId, "x");

		assertThat(site).isEmpty();
	}

	@Test
	void findAllSpaces() {
		final var spaces = client.findAllSpaces(organizationId)
			.limit(1)
			.toList();

		assertThat(spaces).isNotEmpty();
	}

	@Test
	void findAllSpacesWhenUnauthenticated() {
		assertThrows(GitBookClientException.AuthenticationRequired.class, () -> {
			unauthenticatedClient.findAllSpaces(organizationId);
		});
	}

	@Test
	void findAllSpacesWhenNotFound() {
		assertThrows(GitBookClientException.AuthenticationRequired.class, () -> {
			unauthenticatedClient.findAllSpaces("x");
		});
	}

	@Test
	void findSpaceById() {
		final var space = client.findSpaceById(spaceId);

		assertThat(space).isNotEmpty();
	}

	@Test
	void findSpaceByIdWhenUnauthenticated() {
		assertThrows(GitBookClientException.AuthenticationRequired.class, () -> {
			unauthenticatedClient.findSpaceById(spaceId);
		});
	}

	@Test
	void findSpaceByIdWhenNotFound() {
		assertThrows(GitBookClientException.AuthenticationRequired.class, () -> {
			unauthenticatedClient.findSpaceById(spaceId);
		});
	}

	@Test
	void getSpaceContent() {
		final var revisionPage = client.getSpaceContent(spaceId, "/");

		assertThat(revisionPage).isNotEmpty();
	}

	@Test
	void getSpacePages() {
		final var pages = client.getSpacePages(spaceId);

		assertThat(pages)
			.isNotEmpty()
			.hasValueSatisfying((list) -> {
				assertThat(list).isNotEmpty();
			});
	}

	@Test
	void findAllSpaceFiles() {
		final var files = client.findAllSpaceFiles(spaceId)
			.limit(1)
			.toList();

		assertThat(files).isNotEmpty();
	}

	@Test
	void findAllSpaceFilesWhenNotFound() {
		final var files = client.findAllSpaceFiles("x")
			.limit(1)
			.toList();

		assertThat(files).isEmpty();
	}

	@Test
	void findAllChangeRequests() {
		final var spiedClient = spy(client);

		doReturn(Stream.empty(), Stream.empty(), Stream.empty(), Stream.empty())
			.when(spiedClient)
			.findAllChangeRequests(anyString(), any(ChangeRequest.Status.class));

		spiedClient.findAllChangeRequests(spaceId).toList();

		for (ChangeRequest.Status status : ChangeRequest.Status.values()) {
			verify(spiedClient).findAllChangeRequests(eq(spaceId), eq(status));
		}
	}

	@Test
	void findAllChangeRequestsStopIfLimitReached() {
		final var spiedClient = spy(client);

		doReturn(Stream.of(new ChangeRequest(), new ChangeRequest()), Stream.empty(), Stream.empty(), Stream.empty())
			.when(spiedClient)
			.findAllChangeRequests(anyString(), any(ChangeRequest.Status.class));

		spiedClient.findAllChangeRequests(spaceId)
			.limit(1)
			.toList();

		verify(spiedClient, times(1)).findAllChangeRequests(eq(spaceId), eq(ChangeRequest.Status.DRAFT));
		verify(spiedClient, times(0)).findAllChangeRequests(eq(spaceId), eq(ChangeRequest.Status.OPEN));
		verify(spiedClient, times(0)).findAllChangeRequests(eq(spaceId), eq(ChangeRequest.Status.ARCHIVED));
		verify(spiedClient, times(0)).findAllChangeRequests(eq(spaceId), eq(ChangeRequest.Status.MERGED));
	}

	@Test
	void findAllChangeRequestsWithStatus() {
		final var changeRequests = client.findAllChangeRequests(spaceId, ChangeRequest.Status.MERGED)
			.limit(1)
			.toList();

		assertThat(changeRequests).isNotEmpty();
	}

	@Test
	void findAllChangeRequestsWithStatusWhenNotFound() {
		final var changeRequests = client.findAllChangeRequests("x", ChangeRequest.Status.MERGED)
			.limit(1)
			.toList();

		assertThat(changeRequests).isEmpty();
	}

	@Test
	void findChangeRequestById() {
		final var changeRequest = client.findChangeRequestById(spaceId, changeRequestId);

		assertThat(changeRequest).isNotEmpty();
	}

	@Test
	void findChangeRequestByIdWhenNotFound() {
		final var changeRequest = client.findChangeRequestById(spaceId, "x");

		assertThat(changeRequest).isEmpty();
	}

	@Test
	void findChangeRequestByNumber() {
		final var changeRequestReference = client.findChangeRequestById(spaceId, changeRequestId).orElseThrow();
		final var changeRequest = client.findChangeRequestByNumber(spaceId, changeRequestReference.getNumber());

		assertThat(changeRequest).isNotEmpty();
	}

	@Test
	void findChangeRequestByNumberWhenNotFound() {
		final var changeRequest = client.findChangeRequestByNumber(spaceId, 99999);

		assertThat(changeRequest).isEmpty();
	}

	@Test
	void getChangeRequestContent() {
		final var revisionPage = client.getChangeRequestContent(spaceId, changeRequestId, "/");

		assertThat(revisionPage).isNotEmpty();
	}

	@Test
	void getChangeRequestPages() {
		final var pages = client.getChangeRequestPages(spaceId, changeRequestId);

		assertThat(pages)
			.isNotEmpty()
			.hasValueSatisfying((list) -> {
				assertThat(list).isNotEmpty();
			});
	}

	@Test
	@Disabled("currently no change request with files")
	void findAllChangeRequestFiles() {
		final var files = client.findAllChangeRequestFiles(spaceId, changeRequestId)
			.limit(1)
			.toList();

		assertThat(files).isNotEmpty();
	}

}