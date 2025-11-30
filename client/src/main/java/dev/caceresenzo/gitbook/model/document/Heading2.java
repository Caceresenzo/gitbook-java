package dev.caceresenzo.gitbook.model.document;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter(onMethod_ = @JsonCreator)
@Accessors(fluent = true)
public final class Heading2 extends SimpleNode implements Block {

	private final String id;

	private Heading2(String key, List<Node> children, String id) {
		super(key, children);

		this.id = id;
	}

	@JsonCreator
	public static Heading2 fromJson(
		String key,
		List<Node> children,
		String id
	) {
		return new Heading2(key, children, id);
	}

}