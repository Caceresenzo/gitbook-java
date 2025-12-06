package dev.caceresenzo.gitbook.model.document;

public sealed interface Node permits Block, Inline, Text {

	String getKey();

}