package dev.caceresenzo.gitbook.model.document;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter(onMethod_ = @JsonProperty)
@Accessors(fluent = true)
public final class Annotation extends SimpleNode implements Inline {

	private final Fragment body;

	public Annotation(String key, List<Node> children, Fragment body) {
		super(key, children);

		this.body = body;
	}

	@JsonCreator
	public static Annotation fromJson(
		String key,
		List<Node> children,
		Fragment body
	) {
		return new Annotation(key, children, body);
	}

}