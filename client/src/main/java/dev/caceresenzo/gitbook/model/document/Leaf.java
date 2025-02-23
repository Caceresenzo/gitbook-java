package dev.caceresenzo.gitbook.model.document;

import java.util.List;

public record Leaf(
	String text,
	List<Mark> marks
) {}