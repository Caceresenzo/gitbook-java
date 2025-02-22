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
      - [Find a User by an ID](#find-a-user-by-an-id)
    - [Organization](#organization)
      - [Stream Organizations](#stream-organizations)
      - [Find an Organization by an ID](#find-an-organization-by-an-id)
    - [Space](#space)
      - [Stream Spaces](#stream-spaces)
      - [Find a Space by an ID](#find-a-space-by-an-id)
- [Spring Boot Starter](#spring-boot-starter)
  - [Client](#client-1)

# Installation

```xml
<properties>
    <gitbook.version>0.0.0</gitbook.version>
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

#### Find a User by an ID

```java
Optional<User> user = client.findUserById("a0b1c2d3e4f5g6h7i8j9k0l1m2n3");
```

### Organization

#### Stream Organizations

```java
Stream<Organization> organizations = client.findAllOrganizations();

/* or get a list via */
List<Organization> organizations = client.findAllOrganizations().toList();
```

#### Find an Organization by an ID

```java
Optional<Organization> organization = client.findOrganization("a0b1c2d3e4f5g6h7i8j9k0l1m2n3");
```

### Space

#### Stream Spaces

```java
String organizationId = "a0b1c2d3e4f5g6h7i8j9k0l1m2n3";
Stream<Space> spaces = client.findAllSpaces(organizationId);

/* or get a list via */
List<Space> spaces = client.findAllSpaces(organizationId).toList();
```

#### Find a Space by an ID

```java
Optional<Space> space = client.findSpace("a0b1c2d3e4f5g6h7i8j9k0l1m2n3");
```

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
