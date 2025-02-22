package dev.caceresenzo.gitbook.client;

import dev.caceresenzo.gitbook.GitBookException;
import lombok.experimental.StandardException;

@StandardException
@SuppressWarnings("serial")
public class GitBookClientException extends GitBookException {

	@StandardException
	public static class AuthenticationRequired extends GitBookClientException {}

	@StandardException
	public static class InvalidAuthenticationToken extends GitBookClientException {}

	@StandardException
	public static class UserNotFound extends GitBookClientException {}

	@StandardException
	public static class OrganizationNotFound extends GitBookClientException {}

}