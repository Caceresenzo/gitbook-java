package dev.caceresenzo.gitbook.model.document;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class OrderedList extends SimpleNode implements Block {

	private OrderedList(String key, List<Node> children) {
		super(key, children);
	}

	@JsonCreator
	public static OrderedList fromJson(
		String key,
		List<Node> children
	) {
		return new OrderedList(key, children);
	}

}