package dev.caceresenzo.gitbook.client.impl.page;

import java.util.List;

public record Page<T>(
	List<T> items,
	Next next
) {

	public boolean isEmpty() {
		return items == null || items.isEmpty();
	}

	public String nextCursor() {
		if (next == null) {
			return null;
		}

		return next.page();
	}

	public record Next(
		String page
	) {}

}