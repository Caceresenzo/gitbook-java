package dev.caceresenzo.gitbook.model.document;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public sealed interface Inline extends Node {

	List<Node> children();

	record Link(
		String key,
		@JsonProperty("ref") Reference reference,
		List<Node> children
	) implements Inline {}

	record Other(
		String key,
		List<Node> children
	) implements Inline {}

}