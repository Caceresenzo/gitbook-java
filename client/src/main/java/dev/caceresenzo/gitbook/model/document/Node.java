package dev.caceresenzo.gitbook.model.document;

import java.util.List;

public sealed interface Node permits Block, Inline, Text, Other {

	String key();

	List<Node> children();

}