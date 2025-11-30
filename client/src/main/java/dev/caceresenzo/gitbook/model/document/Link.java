package dev.caceresenzo.gitbook.model.document;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter(onMethod_ = @JsonCreator)
@Accessors(fluent = true)
public final class Link extends SimpleNode implements Inline {

	private final Reference reference;

	private Link(String key, List<Node> children, Reference reference) {
		super(key, children);

		this.reference = reference;
	}

	@JsonCreator
	public static Link fromJson(
		String key,
		List<Node> children,
		@JsonProperty("ref") Reference reference
	) {
		return new Link(key, children, reference);
	}

}