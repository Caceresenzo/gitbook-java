package dev.caceresenzo.gitbook.model.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "kind", visible = true)
@JsonSubTypes({
	@JsonSubTypes.Type(value = Reference.Anchor.class, name = "anchor"),
	@JsonSubTypes.Type(value = Reference.File.class, name = "file"),
	@JsonSubTypes.Type(value = Reference.Page.class, name = "page"),
	@JsonSubTypes.Type(value = Reference.Url.class, name = "url"),
})
public sealed interface Reference {

	record Anchor(
		@JsonProperty("anchor") String fragment,
		@JsonProperty("page") String pageId
	) implements Reference {}

	record File(
		@JsonProperty("file") String id
	) implements Reference {}

	record Page(
		@JsonProperty("page") String id
	) implements Reference {}

	record Url(
		String url
	) implements Reference {}

}