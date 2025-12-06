package dev.caceresenzo.gitbook.client.impl;

import dev.caceresenzo.gitbook.client.impl.dto.SpacePagesResponse;
import dev.caceresenzo.gitbook.client.impl.expander.EncodeSlashExpander;
import dev.caceresenzo.gitbook.client.impl.expander.ToLowerStringExpander;
import dev.caceresenzo.gitbook.client.impl.page.Paginated;
import dev.caceresenzo.gitbook.model.ApiInfo;
import dev.caceresenzo.gitbook.model.ChangeRequest;
import dev.caceresenzo.gitbook.model.File;
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

	@RequestLine("GET /v1/orgs?limit={limit}&page={page}")
	Paginated<Organization> getOrganizations(@Param int limit, @Param String page);

	@RequestLine("GET /v1/orgs/{organizationId}")
	Organization getOrganizationById(@Param String organizationId);

	@RequestLine("GET /v1/orgs/{organizationId}/spaces?limit={limit}&page={page}")
	Paginated<Space> getSpaces(@Param String organizationId, @Param int limit, @Param String page);

	@RequestLine("GET /v1/spaces/{spaceId}")
	Space getSpaceById(@Param String spaceId);

	@RequestLine(value = "GET /v1/spaces/{spaceId}/content/path/{pagePath}", decodeSlash = false)
	RevisionPage getSpaceContent(@Param String spaceId, @Param(expander = EncodeSlashExpander.class) String pagePath);

	@RequestLine("GET /v1/spaces/{spaceId}/content/pages?limit={limit}&page={page}")
	SpacePagesResponse getSpacePages(@Param String spaceId, @Param int limit, @Param String page);

	@RequestLine("GET /v1/spaces/{spaceId}/content/files?limit={limit}&page={page}")
	Paginated<File> getSpaceFiles(@Param String spaceId, @Param int limit, @Param String page);

	@RequestLine("GET /v1/spaces/{spaceId}/change-requests?status={status}&limit={limit}&page={page}")
	Paginated<ChangeRequest> getChangeRequests(@Param String spaceId, @Param(expander = ToLowerStringExpander.class) ChangeRequest.Status status, @Param int limit, @Param String page);

	@RequestLine("GET /v1/spaces/{spaceId}/change-requests/{changeRequestId}")
	ChangeRequest getChangeRequestById(@Param String spaceId, @Param String changeRequestId);

	@RequestLine(value = "GET /v1/spaces/{spaceId}/change-requests/{changeRequestId}/content/path/{pagePath}", decodeSlash = false)
	RevisionPage getChangeRequestContent(@Param String spaceId, @Param String changeRequestId, @Param(expander = EncodeSlashExpander.class) String pagePath);

	@RequestLine("GET /v1/spaces/{spaceId}/change-requests/{changeRequestId}/content/pages?limit={limit}&page={page}")
	SpacePagesResponse getChangeRequestPages(@Param String spaceId, @Param String changeRequestId, @Param int limit, @Param String page);

	@RequestLine("GET /v1/spaces/{spaceId}/change-requests/{changeRequestId}/content/files?limit={limit}&page={page}")
	Paginated<File> getChangeRequestFiles(@Param String spaceId, @Param String changeRequestId, @Param int limit, @Param String page);

}