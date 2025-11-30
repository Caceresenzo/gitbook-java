package dev.caceresenzo.gitbook.model.document;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter(onMethod_ = @JsonCreator)
@Accessors(fluent = true)
public final class Text extends SimpleNode implements Node {

	private final List<Leaf> leaves;

	private Text(String key, List<Leaf> leaves) {
		super(key, null);

		this.leaves = leaves;
	}

	@JsonCreator
	public static Text fromJson(
		String key,
		List<Leaf> leaves
	) {
		return new Text(key, leaves);
	}

}