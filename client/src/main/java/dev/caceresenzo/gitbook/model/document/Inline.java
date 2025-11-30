package dev.caceresenzo.gitbook.model.document;

public sealed interface Inline extends Node permits Annotation, Link {}