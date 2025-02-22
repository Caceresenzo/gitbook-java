package dev.caceresenzo.gitbook.client.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.caceresenzo.gitbook.client.GitBookClientException;
import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FeignGitBookErrorDecoder extends ErrorDecoder.Default {

	private final ObjectMapper objectMapper;
	private final List<ErrorMapper> mappers;

	public FeignGitBookErrorDecoder(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;

		this.mappers = new ArrayList<>();
		{
			this.mappers.add(ErrorMapper.equals("This operation requires authentication. Token must be passed in Authorization header", GitBookClientException.AuthenticationRequired::new));
			this.mappers.add(ErrorMapper.equals("Invalid authentication token", GitBookClientException.InvalidAuthenticationToken::new));

			this.mappers.add(ErrorMapper.startsWithAndEndswith("User ", " not found", GitBookClientException.UserNotFound::new));

			this.mappers.add(ErrorMapper.equals("No matching organization found", GitBookClientException.OrganizationNotFound::new));

			this.mappers.add(ErrorMapper.startsWith("No matching space found", GitBookClientException.SpaceNotFound::new));
		}
	}

	@Override
	public Exception decode(String methodKey, Response response) {
		final var exception = (FeignException) super.decode(methodKey, response);

		if (exception instanceof RetryableException) {
			return exception;
		}

		final var message = extractMessage(exception);
		for (final var mapper : mappers) {
			if (mapper.match(message)) {
				throw mapper.map(message, exception);
			}
		}

		throw new GitBookClientException(message, exception);
	}

	public String extractMessage(FeignException exception) {
		try {
			final var responseBody = exception.responseBody().orElseThrow();

			final var dto = objectMapper.readValue(
				responseBody.array(),
				responseBody.arrayOffset(),
				responseBody.limit(),
				ErrorDto.class
			);

			return dto.error().message();
		} catch (Exception __) {
			return null;
		}
	}

	public static record ErrorDto(ErrorContentDto error) {}

	public static record ErrorContentDto(String message) {}

	public interface ErrorMapper {

		boolean match(String message);

		GitBookClientException map(String message, Exception cause);

		static ErrorMapper equals(String exactMessage, BiFunction<String, Exception, GitBookClientException> mapper) {
			return new ErrorMapper() {

				@Override
				public boolean match(String message) {
					return exactMessage.equalsIgnoreCase(message);
				}

				@Override
				public GitBookClientException map(String message, Exception cause) {
					return mapper.apply(message, cause);
				}

			};
		}

		static ErrorMapper startsWith(String prefix, BiFunction<String, Exception, GitBookClientException> mapper) {
			return new ErrorMapper() {

				@Override
				public boolean match(String message) {
					if (message == null) {
						return false;
					}

					return message.startsWith(prefix);
				}

				@Override
				public GitBookClientException map(String message, Exception cause) {
					return mapper.apply(message, cause);
				}

			};
		}

		static ErrorMapper startsWithAndEndswith(String prefix, String suffix, BiFunction<String, Exception, GitBookClientException> mapper) {
			return new ErrorMapper() {

				@Override
				public boolean match(String message) {
					if (message == null) {
						return false;
					}

					return message.startsWith(prefix) && message.endsWith(suffix);
				}

				@Override
				public GitBookClientException map(String message, Exception cause) {
					return mapper.apply(message, cause);
				}

			};
		}

	}

}