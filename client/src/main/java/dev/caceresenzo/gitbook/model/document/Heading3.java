package dev.caceresenzo.gitbook.model.document;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter(onMethod_ = @JsonCreator)
@Accessors(fluent = true)
public final class Heading3 extends SimpleNode implements Block {

	private final String id;

	private Heading3(String key, List<Node> children, String id) {
		super(key, children);

		this.id = id;
	}

	@JsonCreator
	public static Heading3 fromJson(
		String key,
		List<Node> children,
		String id
	) {
		return new Heading3(key, children, id);
	}

}