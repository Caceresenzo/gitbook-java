package dev.caceresenzo.gitbook.model.document;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Leaf {

	@JsonProperty("text")
	private String text;

	@JsonProperty("marks")
	private List<Mark> marks;

}