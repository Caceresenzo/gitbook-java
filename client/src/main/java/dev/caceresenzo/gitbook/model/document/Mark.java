package dev.caceresenzo.gitbook.model.document;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true, defaultImpl = Mark.Other.class)
@JsonSubTypes({
	@JsonSubTypes.Type(value = Mark.Bold.class, name = "bold"),
	@JsonSubTypes.Type(value = Mark.Italic.class, name = "italic"),
	@JsonSubTypes.Type(value = Mark.Strikethrough.class, name = "strikethrough"),
	@JsonSubTypes.Type(value = Mark.Superscript.class, name = "superscript"),
	@JsonSubTypes.Type(value = Mark.Subscript.class, name = "subscript"),
	@JsonSubTypes.Type(value = Mark.Keyboard.class, name = "keyboard"),
	@JsonSubTypes.Type(value = Mark.Code.class, name = "code"),
	@JsonSubTypes.Type(value = Mark.Color.class, name = "color"),
})
public sealed interface Mark {

	record Bold() implements Mark {}

	record Italic() implements Mark {}

	record Strikethrough() implements Mark {}

	record Superscript() implements Mark {}

	record Subscript() implements Mark {}

	record Code() implements Mark {}

	record Keyboard() implements Mark {}

	record Color(
		@JsonProperty("data.background") String background,
		@JsonProperty("data.text") String text
	) implements Mark {}

	record Other(
		String type,
		@JsonAnySetter Map<String, Object> properties
	) implements Mark {}

}