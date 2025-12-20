package dev.caceresenzo.gitbook.client.impl.serial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import dev.caceresenzo.gitbook.model.document.Block;
import dev.caceresenzo.gitbook.model.document.Fragment;

@SuppressWarnings("serial")
public class TableBlockDeserializer extends DelegatingDeserializer {

	private static final TypeReference<List<Fragment>> FRAGMENT_LIST_TYPE = new TypeReference<>() {};

	public TableBlockDeserializer(JsonDeserializer<?> delegate) {
		super(delegate);
	}

	@Override
	protected JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> newDelegatee) {
		return new TableBlockDeserializer(newDelegatee);
	}

	@Override
	public Object deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		final var codec = parser.getCodec();

		final var root = codec.<ObjectNode>readTree(parser);

		final var key = root.remove("key") instanceof TextNode textNode ? textNode.asText() : null;

		final var fragments = root.remove("fragments") instanceof ArrayNode arrayNode
			? codec.readValue(codec.treeAsTokens(arrayNode), FRAGMENT_LIST_TYPE)
			: Collections.<Fragment>emptyList();
		final var framentsByKey = fragments.stream()
			.collect(Collectors.toMap(Fragment::getName, Function.identity()));

		final var data = codec.treeToValue(root.remove("data"), Data.class);

		final var columnDefinitions = new ArrayList<>(data.getDefinition().values());
		columnDefinitions.sort(Comparator.comparingInt((definition) -> data.view.getColumns().indexOf(definition.getId())));

		final var rows = data.getRecords().values()
			.stream()
			.sorted(Comparator.comparing(Data.RecordEntry::getOrderIndex))
			.map((recordEntry) -> {
				final var cells = new HashMap<String, Fragment>(recordEntry.values.size());

				for (final var entry : recordEntry.values.entrySet()) {
					final var columnId = entry.getKey();
					final var fragmentKey = entry.getValue();

					final var fragment = framentsByKey.get(fragmentKey);

					cells.put(columnId, fragment);
				}

				final var row = new Block.Table.Row();
				row.setCells(cells);

				return row;
			})
			.toList();

		final var table = new Block.Table();
		table.setKey(key);
		table.setChildren(Collections.emptyList());
		table.setHideHeader(data.view.hideHeader);
		table.setColumnDefinitions(columnDefinitions);
		table.setRows(rows);

		return table;
	}

	@lombok.Data
	public static class Data {

		@JsonProperty
		public Map<String, RecordEntry> records;

		@JsonProperty
		public Map<String, Block.Table.ColumnDefinition> definition;

		@JsonProperty
		public View view;

		@lombok.Data
		public static class RecordEntry {

			@JsonProperty
			public Map<String, Object> values;

			@JsonProperty
			public String orderIndex;

		}

		@lombok.Data
		public static class View {

			@JsonProperty
			public List<String> columns;

			@JsonProperty
			public boolean hideHeader;

		}

	}

}