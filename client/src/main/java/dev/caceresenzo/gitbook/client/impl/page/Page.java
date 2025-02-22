package dev.caceresenzo.gitbook.client.impl.page;

import java.util.List;
import java.util.stream.Stream;

public record Page<T>(
	List<T> items
) {

	public boolean isEmpty() {
		return items == null || items.isEmpty();
	}

	public Stream<T> stream() {
		if (isEmpty()) {
			return Stream.empty();
		}

		return items.stream();
	}

}