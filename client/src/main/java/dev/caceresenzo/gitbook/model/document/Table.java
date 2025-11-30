package dev.caceresenzo.gitbook.model.document;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter(onMethod_ = @JsonCreator)
@Accessors(fluent = true)
public final class Table extends SimpleNode implements Block {

	private final Map<String, Object> records;

	private Table(String key, Map<String, Object> records) {
		super(key, Collections.emptyList());

		this.records = records;
	}

	@JsonCreator
	public static Table fromJson(
		String key,
		Map<String, Object> records
	) {
		return new Table(key, records);
	}

}