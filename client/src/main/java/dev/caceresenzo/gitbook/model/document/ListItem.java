package dev.caceresenzo.gitbook.model.document;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class ListItem extends SimpleNode implements Block {

	private ListItem(String key, List<Node> children) {
		super(key, children);
	}

	@JsonCreator
	public static ListItem fromJson(
		String key,
		List<Node> children
	) {
		return new ListItem(key, children);
	}

}