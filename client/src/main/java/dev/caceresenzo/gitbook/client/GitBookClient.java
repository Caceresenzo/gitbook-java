package dev.caceresenzo.gitbook.client;

import java.util.Optional;

import dev.caceresenzo.gitbook.client.impl.GitBookClientImpl;
import dev.caceresenzo.gitbook.model.ApiInfo;
import dev.caceresenzo.gitbook.model.User;
import lombok.Data;
import lombok.experimental.Accessors;

public interface GitBookClient {

	/**
	 * Get current version and build of the API.
	 */
	ApiInfo getApiInfo();

	Optional<User> findCurrentUser();

	Optional<User> findUserById(String id);

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

		/** The URL of the GitBook API. Defaults to `https://api.gitbook.com`. */
		private String apiUrl = DEFAULT_API_URL;

		/** The access token. */
		private String accessToken;

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
				accessToken
			);
		}

	}

}