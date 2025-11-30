package dev.caceresenzo.gitbook.client.impl.page;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import lombok.Data;

@Data
public class PageSpliterator<T> implements Spliterator<T> {

	private final Function<String, Paginated<T>> nextPageGetter;

	private Iterator<T> currentIterator;
	private String nextCursor;

	public PageSpliterator(Paginated<T> firstPage, Function<String, Paginated<T>> nextPageGetter) {
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

			final var nextPage = nextPageGetter.apply(nextCursor);
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

}