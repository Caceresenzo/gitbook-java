package dev.caceresenzo.gitbook.model.document;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class Paragraph extends SimpleNode implements Block {

	private Paragraph(String key, List<Node> children) {
		super(key, children);
	}

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public static Paragraph fromJson(
		String key,
		List<Node> children
	) {
		return new Paragraph(key, children);
	}

}