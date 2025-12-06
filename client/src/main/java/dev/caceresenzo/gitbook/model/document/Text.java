package dev.caceresenzo.gitbook.model.document;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public final class Text implements Node {

	@JsonProperty("key")
	private String key;

	@JsonProperty("leaves")
	private List<Leaf> leaves;

}