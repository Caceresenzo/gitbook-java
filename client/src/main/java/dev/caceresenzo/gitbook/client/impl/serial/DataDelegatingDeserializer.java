package dev.caceresenzo.gitbook.client.impl.serial;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@SuppressWarnings("serial")
public class DataDelegatingDeserializer extends DelegatingDeserializer {

	public DataDelegatingDeserializer(JsonDeserializer<?> delegate) {
		super(delegate);
	}

	@Override
	protected JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> delegate) {
		return new DataDelegatingDeserializer(delegate);
	}

	@Override
	public Object deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		final var codec = parser.getCodec();

		final var root = codec.<ObjectNode>readTree(parser);

		if (root.remove("data") instanceof ObjectNode data) {
			final var iterator = data.fields();

			while (iterator.hasNext()) {
				final var entry = iterator.next();
				root.set("data.%s".formatted(entry.getKey()), entry.getValue());
			}
		}

		if (root.remove("meta") instanceof ObjectNode meta) {
			final var iterator = meta.fields();

			while (iterator.hasNext()) {
				final var entry = iterator.next();
				root.set("meta.%s".formatted(entry.getKey()), entry.getValue());
			}
		}

		if (root.remove("fragments") instanceof ArrayNode fragments) {
			for (final var fragment : fragments) {
				if (fragment instanceof ObjectNode fragmentObject) {
					final var name = fragmentObject.get("fragment").asText();

					root.set("fragment.%s".formatted(name), fragmentObject);
				}
			}
		}

		parser = codec.treeAsTokens(root);
		parser.nextToken();

		return getDelegatee().deserialize(parser, context);
	}

}