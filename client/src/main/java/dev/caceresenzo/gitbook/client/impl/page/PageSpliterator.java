package dev.caceresenzo.gitbook.client.impl.page;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import lombok.Data;

@Data
public class PageSpliterator<T> implements Spliterator<T> {

	private final int pageSize;
	private final NextPageGetter<T> nextPageGetter;

	private Iterator<T> currentIterator;
	private String nextCursor;

	public PageSpliterator(
		int pageSize,
		NextPageGetter<T> nextPageGetter,
		Paginated<T> firstPage
	) {
		this.pageSize = pageSize;
		this.nextPageGetter = Objects.requireNonNull(nextPageGetter);

		setPage(firstPage);
	}

	@Override
	public boolean tryAdvance(Consumer<? super T> action) {
		if (currentIterator == null) {
			return false;
		}

		if (!currentIterator.hasNext()) {
			if (nextCursor == null) {
				return false;
			}

			final var nextPage = nextPageGetter.fetch(pageSize, nextCursor);
			if (nextPage.isEmpty()) {
				return false;
			}

			setPage(nextPage);
		}

		action.accept(currentIterator.next());
		return true;
	}

	@Override
	public Spliterator<T> trySplit() {
		return null;
	}

	@Override
	public long estimateSize() {
		return Long.MAX_VALUE;
	}

	@Override
	public int characteristics() {
		return ORDERED | NONNULL;
	}

	public void setPage(Paginated<T> page) {
		this.currentIterator = page.items().iterator();
		this.nextCursor = page.nextCursor();
	}

	public Stream<T> asStream() {
		return StreamSupport.stream(this, false);
	}

	public static <T> PageSpliterator<T> of(int pageSize, NextPageGetter<T> nextPageGetter) {
		final var firstPage = nextPageGetter.fetch(pageSize, null);

		return new PageSpliterator<>(
			pageSize,
			nextPageGetter,
			firstPage
		);
	}

}