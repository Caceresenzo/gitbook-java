package dev.caceresenzo.gitbook.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = GitBookProperties.PREFIX)
public class GitBookProperties {

	public static final String PREFIX = "gitbook";
	public static final String PREFIX_ACCESS_TOKEN = PREFIX + ".access-token";

	private String apiUrl;
	private String accessToken;

}