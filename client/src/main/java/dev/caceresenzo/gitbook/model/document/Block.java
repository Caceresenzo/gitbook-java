package dev.caceresenzo.gitbook.model.document;

import java.util.List;
import java.util.Map;

public sealed interface Block extends Node {

	List<Node> children();

	record Heading1(
		String key,
		List<Node> children
	) implements Block {}

	record Heading2(
		String key,
		List<Node> children
	) implements Block {}

	record Heading3(
		String key,
		List<Node> children
	) implements Block {}

	record Paragraph(
		String key,
		List<Node> children
	) implements Block {}

	record ListItem(
		String key,
		List<Node> children
	) implements Block {}

	record OrderedList(
		String key,
		List<Node> children
	) implements Block {}

	record UnorderedList(
		String key,
		List<Node> children
	) implements Block {}

	record Table(
		String key,
		Map<String, Object> records
	) implements Block {

		@Override
		public List<Node> children() {
			return null;
		}

	}

}