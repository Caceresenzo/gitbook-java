# Gitbook Client for Java

This Java client connects with GitBook.com, enabling simple interaction with their API. It also includes a Spring Boot starter for quick integration.

- [Gitbook Client for Java](#gitbook-client-for-java)
- [Installation](#installation)
- [Client](#client)
	- [Configuration](#configuration)
	- [Usage](#usage)
		- [API](#api)
			- [Get API Informations](#get-api-informations)
		- [User](#user)
			- [Find the Currently Authenticated User](#find-the-currently-authenticated-user)
			- [Find a User by ID](#find-a-user-by-id)
		- [Organization](#organization)
			- [Stream Organizations](#stream-organizations)
			- [Find an Organization by ID](#find-an-organization-by-id)
		- [Space](#space)
			- [Stream Spaces](#stream-spaces)
			- [Find a Space by ID](#find-a-space-by-id)
			- [Get a Space Content page by path](#get-a-space-content-page-by-path)
			- [List all Space Pages](#list-all-space-pages)
			- [Stream Space Files](#stream-space-files)
		- [Change Requests](#change-requests)
			- [Stream Change Requests](#stream-change-requests)
			- [Find a Change Requests by ID](#find-a-change-requests-by-id)
			- [Find a Change Requests by Space Number](#find-a-change-requests-by-space-number)
			- [Get a Change Request Content page by path](#get-a-change-request-content-page-by-path)
			- [List all Change Request Pages](#list-all-change-request-pages)
			- [Stream Change Request Files](#stream-change-request-files)
	- [Document Node Objects](#document-node-objects)
- [Spring Boot Starter](#spring-boot-starter)
	- [Client](#client-1)

# Installation

```xml
<properties>
	<gitbook.version>0.1.0</gitbook.version>
</properties>

<dependencies>
	<dependency>
		<groupId>dev.caceresenzo.gitbook</groupId>
		<artifactId>gitbook-client</artifactId>
		<version>${gitbook.version}</version>
	</dependency>
</dependencies>
```

# Client

## Configuration

```java
GitBookClient client = GitBookClient.builder()
	.accessToken("gb_api_a0b1c2d3e4f5g6h7i8j9k0l1m2n3o4p5q6r7s8t9")
	.build();

/* stay unauthenticated */
GitBookClient client = GitBookClient.builder()
	.unauthenticated()
	.build();
```

## Usage

### API

#### Get API Informations

```java
ApiInfo info = client.getApiInfo();
```

### User

#### Find the Currently Authenticated User

The user will not be found if the access token is missing or invalid.

```java
Optional<User> user = client.findCurrentUser();
```

#### Find a User by ID

```java
String userId = "a0b1c2d3e4f5g6h7i8j9k0l1m2n3";
Optional<User> user = client.findUserById(userId);
```

### Organization

#### Stream Organizations

```java
Stream<Organization> organizations = client.findAllOrganizations();

/* or get a list via */
List<Organization> organizations = client.findAllOrganizations().toList();
```

#### Find an Organization by ID

```java
String organizationId = "a0b1c2d3e4f5g6h7i8j9k0l1m2n3";
Optional<Organization> organization = client.findOrganization(organizationId);
```

### Space

#### Stream Spaces

```java
String organizationId = "a0b1c2d3e4f5g6h7i8j9k0l1m2n3";
Stream<Space> spaces = client.findAllSpaces(organizationId);

/* or get a list via */
List<Space> spaces = client.findAllSpaces(organizationId).toList();
```

#### Find a Space by ID

```java
String spaceId = "a0b1c2d3e4f5g6h7i8j9k0l1m2n3";
Optional<Space> space = client.findSpace(spaceId);
```

#### Get a Space Content page by path

```java
String spaceId = "a0b1c2d3e4f5g6h7i8j9k0l1m2n3";
String pagePath = "/hello";
Optional<RevisionPage> content = client.getSpaceContent(spaceId, pagePath);
```

#### List all Space Pages

```java
String spaceId = "a0b1c2d3e4f5g6h7i8j9k0l1m2n3";
Optional<List<Page>> content = client.getSpacePages(spaceId);
```

#### Stream Space Files

```java
String spaceId = "a0b1c2d3e4f5g6h7i8j9k0l1m2n3";
Stream<File> files = client.findAllSpaceFiles(spaceId);

/* or get a list via */
List<File> files = client.findAllSpaceFiles(spaceId).toList();
```

### Change Requests

#### Stream Change Requests

```java
String spaceId = "a0b1c2d3e4f5g6h7i8j9k0l1m2n3";
Stream<ChangeRequest> changeRequests = client.findAllChangeRequests(spaceId);

/* or get a list via */
List<ChangeRequest> changeRequests = client.findAllChangeRequests(spaceId).toList();

/* or only filter by status */
Stream<ChangeRequest> archivedChangeRequests = client.findAllChangeRequests(spaceId, ChangeRequest.Status.ARCHIVED);
List<ChangeRequest> archivedChangeRequests = client.findAllChangeRequests(spaceId, ChangeRequest.Status.ARCHIVED).toList();
```

#### Find a Change Requests by ID

```java
String spaceId = "a0b1c2d3e4f5g6h7i8j9k0l1m2n3";
String changeRequestId = "a0b1c2d3e4f5g6h7i8j9k0l1m2n3";
Optional<ChangeRequest> changeRequest = client.findChangeRequestById(spaceId, changeRequestId);
```

#### Find a Change Requests by Space Number

```java
String spaceId = "a0b1c2d3e4f5g6h7i8j9k0l1m2n3";
String changeRequestNumber = 42;
Optional<ChangeRequest> changeRequest = client.findChangeRequestByNumber(spaceId, changeRequestNumber);
```

#### Get a Change Request Content page by path

```java
String spaceId = "a0b1c2d3e4f5g6h7i8j9k0l1m2n3";
String changeRequestId = "a0b1c2d3e4f5g6h7i8j9k0l1m2n3";
String pagePath = "/hello";
Optional<RevisionPage> content = client.getChangeRequestContent(spaceId, changeRequestId, pagePath);
```

#### List all Change Request Pages

```java
String spaceId = "a0b1c2d3e4f5g6h7i8j9k0l1m2n3";
String changeRequestId = "a0b1c2d3e4f5g6h7i8j9k0l1m2n3";
Optional<List<Page>> pages = client.getChangeRequestPages(spaceId, changeRequestId);
```

#### Stream Change Request Files

```java
String spaceId = "a0b1c2d3e4f5g6h7i8j9k0l1m2n3";
String changeRequestId = "a0b1c2d3e4f5g6h7i8j9k0l1m2n3";
Stream<File> files = client.findAllChangeRequestFiles(spaceId, changeRequestId);

/* or get a list via */
List<File> files = client.findAllChangeRequestFiles(spaceId, changeRequestId).toList();
```

## Document Node Objects

All document objects have a corresponding Java class, making it easy to query information about the content.
Most of these classes have children that can be used to traverse the tree.

<details>
<summary>Testing the block types</summary>

```java
switch (block) {
	case Block.Code code -> {};
	case Block.CodeLine codeLine -> { }

	case Block.Columns columns -> { }
	case Block.Column column -> { }

	case Block.Divider divider -> { }

	case Block.Drawing drawing -> { }

	case Block.Embed embed -> { }

	case Block.Expandable expandable -> { }

	case Block.Heading1 heading1 -> { }
	case Block.Heading2 heading2 -> { }
	case Block.Heading3 heading3 -> { }

	case Block.Hint hint -> { }

	case Block.Images images -> { }
	case Block.Image image -> { }

	case Block.ListItem listItem -> { }
	case Block.OrderedList orderedList -> { }
	case Block.UnorderedList unorderedList -> { }
	case Block.TaskList taskList -> { }

	case Block.Math math -> { }

	case Block.PageLink pageLink -> { }

	case Block.Paragraph paragraph -> { }

	case Block.Quote quote -> { }

	case Block.Stepper stepper -> { }
	case Block.StepperStep stepperStep -> { }

	case Block.Table table -> { }

	case Block.Tabs tabs -> { }
	case Block.TabsItem tabsItem -> { }

	case Block.Updates updates -> { }
	case Block.Update update -> { }

	/* Fallback */
	case Block.Other other -> { }
}
```

</details>

<details>
<summary>Testing the inline types</summary>

```java
switch (inline) {
	case Inline.Annotation annotation -> { }
	case Inline.Link link -> { }
	case Inline.Mention mention -> { }
	case Inline.Math math -> { }
	case Inline.Button button -> { }
	case Inline.Emoji emoji -> { }
	case Inline.Icon icon -> { }
	case Inline.Image image -> { }

	/* Fallback */
	case Inline.Other other -> { }
}
```

</details>

<details>
<summary>Testing the mark types</summary>

```java
switch (mark) {
	case Mark.Bold __ -> { }
	case Mark.Italic __ -> { }
	case Mark.Strikethrough __ -> { }

	case Mark.Superscript __ -> { }
	case Mark.Subscript __ -> { }

	case Mark.Code __ -> { }
	case Mark.Keyboard __ -> { }

	case Mark.Color color -> { }

	/* Fallback */
	case Mark.Other __ -> { }
}
```

</details>

# Spring Boot Starter

There is a Spring Boot auto-configuration available.

```xml
<dependencies>
	<dependency>
		<groupId>dev.caceresenzo.gitbook</groupId>
		<artifactId>gitbook-spring-boot-starter</artifactId>
		<version>${gitbook.version}</version>
	</dependency>
</dependencies>
```

## Client

This is always enabled. An access token can be specified in the configuration:

```yml
gitbook:
  access-token: gb_api_a0b1c2d3e4f5g6h7i8j9k0l1m2n3o4p5q6r7s8t9
```
