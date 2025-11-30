package dev.caceresenzo.gitbook.model.document;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter(onMethod_ = @JsonCreator)
@Accessors(fluent = true)
@RequiredArgsConstructor
public class Fragment {

	private final String key;
	private final List<Node> nodes;
	private final String name;

	@JsonCreator
	public static Fragment fromJson(
		String key,
		List<Node> nodes,
		String name
	) {
		return new Fragment(key, nodes, name);
	}

}