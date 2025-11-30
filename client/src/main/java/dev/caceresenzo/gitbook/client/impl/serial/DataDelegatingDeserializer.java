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
			root.setAll(data);
		}

		if (root.remove("meta") instanceof ObjectNode meta) {
			root.setAll(meta);
		}

		if (root.remove("nodes") instanceof ArrayNode children) {
			root.set("children", children);
		}

		System.out.println(root);

		parser = codec.treeAsTokens(root);
		parser.nextToken();

		return getDelegatee().deserialize(parser, context);
	}

}