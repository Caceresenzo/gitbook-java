package dev.caceresenzo.gitbook.model.document;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class Code extends SimpleNode implements Block {

	private Code(String key, List<Node> children) {
		super(key, children);
	}

	@JsonCreator
	public static Code fromJson(
		String key,
		List<Node> children
	) {
		return new Code(key, children);
	}

}