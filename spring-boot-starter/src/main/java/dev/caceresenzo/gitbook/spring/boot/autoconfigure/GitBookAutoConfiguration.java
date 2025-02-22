package dev.caceresenzo.gitbook.spring.boot.autoconfigure;

import java.io.IOException;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.caceresenzo.gitbook.client.GitBookClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(GitBookClient.class)
@EnableConfigurationProperties(GitBookProperties.class)
public class GitBookAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	GitBookClient gitBookClient(GitBookProperties properties) throws IOException {
		log.info("Configuring GitBook Client");

		final GitBookClient.Builder builder = GitBookClient.builder()
			.accessToken(properties.getAccessToken());

		final var apiUrl = properties.getApiUrl();
		if (apiUrl != null) {
			builder.apiUrl(apiUrl);
		}

		final var accessToken = properties.getAccessToken();
		if (accessToken != null) {
			builder.accessToken(accessToken);
		} else {
			builder.unauthenticated();
			log.info("No Access Token specified, using unauthenticated mode");
		}

		final var maxPageSize = properties.getMaxPageSize();
		if (maxPageSize != null) {
			builder.maxPageSize(maxPageSize);
		}

		return builder.build();
	}

}