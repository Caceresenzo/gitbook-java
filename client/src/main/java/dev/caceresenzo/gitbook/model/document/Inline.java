package dev.caceresenzo.gitbook.model.document;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

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

		@JsonProperty("ref")
		private Reference reference;

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Mention extends SimpleInline implements Inline {

		@JsonProperty("ref")
		private Reference reference;

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Math extends SimpleInline implements Inline {

		@JsonProperty("data.formula")
		private String formula;

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
		return "%s(key=\"%s\")".formatted(getClass().getSimpleName(), key);
	}

}