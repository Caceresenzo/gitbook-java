# Gitbook Client for Java

This Java client connects with GitBook.com, enabling simple interaction with their API. It also includes a Spring Boot starter for quick integration.

- [Gitbook Client for Java](#gitbook-client-for-java)
- [Installation](#installation)
- [Client](#client)
  - [Configuration](#configuration)
  - [Usage](#usage)
    - [API Info](#api-info)
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

### API Info

```java
ApiInfo info = client.getApiInfo();
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
