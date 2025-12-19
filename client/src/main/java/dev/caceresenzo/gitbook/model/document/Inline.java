package dev.caceresenzo.gitbook.model.document;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true, defaultImpl = Inline.Other.class)
@JsonSubTypes({
	@JsonSubTypes.Type(value = Inline.Annotation.class, name = "annotation"),
	@JsonSubTypes.Type(value = Inline.Link.class, name = "link"),
	@JsonSubTypes.Type(value = Inline.Mention.class, name = "mention"),
	@JsonSubTypes.Type(value = Inline.Math.class, name = "inline-math"),
	@JsonSubTypes.Type(value = Inline.Button.class, name = "button"),
	@JsonSubTypes.Type(value = Inline.Emoji.class, name = "emoji"),
	@JsonSubTypes.Type(value = Inline.Icon.class, name = "icon"),
	@JsonSubTypes.Type(value = Inline.Image.class, name = "inline-image"),
})
public sealed interface Inline extends Node {

	List<Node> getChildren();

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Annotation extends SimpleInline implements Inline {

		@JsonProperty("fragment.annotation-body")
		private Fragment body;

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Link extends SimpleInline implements Inline {

		@JsonProperty("data.ref")
		private Reference reference;

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Mention extends SimpleInline implements Inline {

		@JsonProperty("data.ref")
		private Reference reference;

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Math extends SimpleInline implements Inline {

		@JsonProperty("data.formula")
		private String formula;

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Button extends SimpleInline implements Inline {

		@JsonProperty("data.ref")
		private Reference reference;

		@JsonProperty("data.label")
		private String label;

		@JsonProperty("data.kind")
		private Kind kind;

		@JsonProperty("data.icon")
		private String icon;

		public enum Kind {

			@JsonProperty("primary")
			PRIMARY,

			@JsonProperty("secondary")
			SECONDARY;
		}

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Emoji extends SimpleInline implements Inline {

		@JsonProperty("data.code")
		private String code;

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Icon extends SimpleInline implements Inline {

		@JsonProperty("data.icon")
		private String icon;

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Image extends SimpleInline implements Inline {

		@JsonProperty("data.alt")
		private String alt;

		@JsonProperty("data.ref")
		private Reference source;

		@JsonProperty("data.size")
		private String size;

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Other extends SimpleInline implements Inline {

		@JsonProperty("type")
		private String type;

		@JsonAnySetter
		@JsonAnyGetter
		private Map<String, Object> properties;

		@Override
		public String toString() {
			return "%s(key=\"%s\", type=%s, children=%s)".formatted(getClass().getSimpleName(), getKey(), type, getChildren());
		}

	}

}

@Data
class SimpleInline {

	@JsonProperty("key")
	private String key;

	@JsonProperty("nodes")
	private List<Node> children;

	@Override
	public String toString() {
		return "%s(key=\"%s\", children=%s)".formatted(getClass().getSimpleName(), key, children);
	}

}