package dev.caceresenzo.gitbook.model.document;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter(onMethod_ = @JsonCreator)
@Accessors(fluent = true)
public final class Image extends SimpleNode implements Block {

	private final String alt;
	private final Reference reference;
	private final Fragment caption;

	private Image(String key, List<Node> children, String alt, Reference reference, Fragment caption) {
		super(key, children);

		this.alt = alt;
		this.reference = reference;
		this.caption = caption;
	}

	@JsonCreator
	public static Image fromJson(
		String key,
		List<Node> children,
		String alt,
		@JsonProperty("ref") Reference reference,
		Fragment caption
	) {
		return new Image(key, children, alt, reference, caption);
	}

}