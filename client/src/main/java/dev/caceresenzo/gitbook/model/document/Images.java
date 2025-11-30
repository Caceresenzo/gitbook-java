package dev.caceresenzo.gitbook.model.document;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class Images extends SimpleNode implements Block {

	private Images(String key, List<Node> children) {
		super(key, children);
	}

	@JsonCreator
	public static Images fromJson(
		String key,
		List<Node> children
	) {
		return new Images(key, children);
	}

}