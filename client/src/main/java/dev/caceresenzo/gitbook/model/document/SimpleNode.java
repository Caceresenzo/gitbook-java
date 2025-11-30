package dev.caceresenzo.gitbook.model.document;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter(onMethod_ = @JsonCreator)
@Accessors(fluent = true)
@RequiredArgsConstructor
class SimpleNode {

	private final String key;
	private final List<Node> children;

}