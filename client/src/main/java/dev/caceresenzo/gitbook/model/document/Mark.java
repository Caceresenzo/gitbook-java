package dev.caceresenzo.gitbook.model.document;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true, defaultImpl = Mark.Other.class)
@JsonSubTypes({
	@JsonSubTypes.Type(value = Mark.Bold.class, name = "bold"),
	@JsonSubTypes.Type(value = Mark.Code.class, name = "code"),
	@JsonSubTypes.Type(value = Mark.Color.class, name = "color"),
	@JsonSubTypes.Type(value = Mark.Italic.class, name = "italic"),
	@JsonSubTypes.Type(value = Mark.Strikethrough.class, name = "strikethrough"),
})
public sealed interface Mark {

	record Bold() implements Mark {}

	record Code() implements Mark {}

	record Color(
		String background,
		String text
	) implements Mark {}

	record Italic() implements Mark {}

	record Strikethrough() implements Mark {}

	record Other(
		String type,
		@JsonAnySetter Map<String, Object> properties
	) implements Mark {}

}