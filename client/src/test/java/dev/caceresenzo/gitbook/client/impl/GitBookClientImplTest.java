package dev.caceresenzo.gitbook.client.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import dev.caceresenzo.gitbook.client.GitBookClient;

class GitBookClientImplTest {

	@Test
	void getApiInfo() {
		final var client = GitBookClient.builder().unauthenticated().build();

		final var apiInfo = client.getApiInfo();

		assertNotNull(apiInfo);
		assertNotNull(apiInfo.getVersion());
		assertNotNull(apiInfo.getBuild());
	}

}