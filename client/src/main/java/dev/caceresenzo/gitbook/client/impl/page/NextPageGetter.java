package dev.caceresenzo.gitbook.client.impl.page;

@FunctionalInterface
public interface NextPageGetter<T> {

	Paginated<T> fetch(int pageSize, String cursor);

}