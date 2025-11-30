package dev.caceresenzo.gitbook.model.document;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class UnorderedList extends SimpleNode implements Block {

	private UnorderedList(String key, List<Node> children) {
		super(key, children);
	}

	@JsonCreator
	public static UnorderedList fromJson(
		String key,
		List<Node> children
	) {
		return new UnorderedList(key, children);
	}

}