package dev.caceresenzo.gitbook.model.document;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Fragment {

	@JsonProperty("key")
	private final String key;

	@JsonProperty("nodes")
	private final List<Node> nodes;

	@JsonProperty("fragment")
	private final String name;

	public boolean hasNodes() {
		return nodes != null && !nodes.isEmpty();
	}

}