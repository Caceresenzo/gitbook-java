package dev.caceresenzo.gitbook.client.impl;

import dev.caceresenzo.gitbook.client.impl.expander.EncodeSlashExpander;
import dev.caceresenzo.gitbook.client.impl.page.Page;
import dev.caceresenzo.gitbook.model.ApiInfo;
import dev.caceresenzo.gitbook.model.Organization;
import dev.caceresenzo.gitbook.model.RevisionPage;
import dev.caceresenzo.gitbook.model.Space;
import dev.caceresenzo.gitbook.model.User;
import feign.Param;
import feign.RequestLine;

public interface FeignGitBookClient {

	String JSON_CONTENT_TYPE = "Content-Type: application/json";

	@RequestLine("GET /v1/")
	ApiInfo getApiInfo();

	@RequestLine("GET /v1/user")
	User getAuthenticatedUser();

	@RequestLine("GET /v1/users/{userId}")
	User getUserById(@Param String userId);

	@RequestLine("GET /v1/orgs?limit={limit}")
	Page<Organization> getOrganizations(@Param long limit);

	@RequestLine("GET /v1/orgs?limit={limit}&page={page}")
	Page<Organization> getOrganizations(@Param long limit, @Param String page);

	@RequestLine("GET /v1/orgs/{organizationId}")
	Organization getOrganizationById(@Param String organizationId);

	@RequestLine("GET /v1/orgs/{organizationId}/spaces?limit={limit}")
	Page<Space> getSpaces(@Param String organizationId, @Param long limit);

	@RequestLine("GET /v1/orgs/{organizationId}/spaces?limit={limit}&page={page}")
	Page<Space> getSpaces(@Param String organizationId, @Param long limit, @Param String page);

	@RequestLine("GET /v1/spaces/{spaceId}")
	Space getSpaceById(@Param String spaceId);

	@RequestLine(value = "GET /v1/spaces/{spaceId}/content/path/{pagePath}", decodeSlash = false)
	RevisionPage getSpaceContent(@Param String spaceId, @Param(expander = EncodeSlashExpander.class) String pagePath);

}