package dev.caceresenzo.gitbook.model.document;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class CodeLine extends SimpleNode implements Block {

	private CodeLine(String key, List<Node> children) {
		super(key, children);
	}

	@JsonCreator
	public static CodeLine fromJson(
		String key,
		List<Node> children
	) {
		return new CodeLine(key, children);
	}

}