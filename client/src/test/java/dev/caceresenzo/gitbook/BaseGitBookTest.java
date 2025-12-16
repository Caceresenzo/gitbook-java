package dev.caceresenzo.gitbook;

import org.junit.jupiter.api.BeforeAll;

import dev.caceresenzo.gitbook.client.GitBookClient;

public class BaseGitBookTest {

	public static final String ACCESS_TOKEN_ENV_VAR = "GITBOOK_ACCESS_TOKEN";

	protected static GitBookClient client;

	@BeforeAll
	static void setUp() {
		final var accessToken = assertEnvironmentVariable(ACCESS_TOKEN_ENV_VAR);

		client = GitBookClient.builder()
			.accessToken(accessToken)
			.trace(true)
			.build();
	}

	protected static String assertEnvironmentVariable(String name) {
		String value = System.getenv(name);
		if (value == null || value.isBlank()) {
			throw new IllegalStateException("%s is not set".formatted(name));
		}

		return value;
	}

}