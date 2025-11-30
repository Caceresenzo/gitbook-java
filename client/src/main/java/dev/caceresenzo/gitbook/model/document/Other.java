package dev.caceresenzo.gitbook.model.document;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter(onMethod_ = @JsonCreator)
@Accessors(fluent = true)
public final class Other extends SimpleNode implements Node {

	private final String type;

	@JsonAnyGetter
	private final Map<String, Object> properties;

	private Other(String key, List<Node> children, String type, Map<String, Object> properties) {
		super(key, children);

		this.type = type;
		this.properties = properties;
	}

	@JsonCreator
	public static Other fromJson(
		String key,
		List<Node> children,
		String object,
		String type,
		@JsonAnySetter Map<String, Object> properties
	) {
		return new Other(key, children, type, properties);
	}

}