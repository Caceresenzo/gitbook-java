package dev.caceresenzo.gitbook.model.document;

import java.util.List;

public sealed interface Node permits Block, Inline, Node.Text {

	String key();

	record Text(
		String key,
		List<Leaf> leaves
	) implements Node {}

}