package dev.caceresenzo.gitbook.client.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dev.caceresenzo.gitbook.BaseGitBookTest;
import dev.caceresenzo.gitbook.client.GitBookClient;
import dev.caceresenzo.gitbook.client.GitBookClientException;

class GitBookClientImplTest extends BaseGitBookTest {

	static GitBookClient unauthenticatedClient;

	@BeforeAll
	static void setUp() {
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
		final var organizations = client.findAllOrganizations().toList();

		assertThat(organizations).hasSizeGreaterThanOrEqualTo(1);
	}

	@Test
	void findAllOrganizationsWhenUnauthenticated() {
		assertThrows(GitBookClientException.AuthenticationRequired.class, unauthenticatedClient::findAllOrganizations);
	}

	@Test
	void findOrganizationById() {
		final var firstOrganization = client.findAllOrganizations()
			.findFirst()
			.orElseThrow();

		final var organization = client.findOrganizationById(firstOrganization.getId());

		assertThat(organization)
			.contains(firstOrganization);
	}

	@Test
	void findOrganizationByIdWhenUnauthenticated() {
		final var firstOrganization = client.findAllOrganizations()
			.findFirst()
			.orElseThrow();

		final var organization = unauthenticatedClient.findOrganizationById(firstOrganization.getId());

		assertThat(organization)
			.contains(firstOrganization);
	}

	@Test
	void findOrganizationByIdWhenNotFound() {
		final var organization = unauthenticatedClient.findOrganizationById("x");

		assertThat(organization).isEmpty();
	}

}