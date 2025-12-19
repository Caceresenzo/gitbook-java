package dev.caceresenzo.gitbook.client.impl.serial;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import dev.caceresenzo.gitbook.model.document.Block;
import dev.caceresenzo.gitbook.model.document.Inline;
import dev.caceresenzo.gitbook.model.document.Node;
import dev.caceresenzo.gitbook.model.document.Text;

@SuppressWarnings("serial")
public class NodeDelegatingDeserializer extends DelegatingDeserializer {

	private static final Map<String, Class<? extends Node>> TYPES = new HashMap<>();

	static {
		TYPES.put("block", Block.class);
		TYPES.put("inline", Inline.class);
		TYPES.put("text", Text.class);
	}

	public NodeDelegatingDeserializer(JsonDeserializer<?> delegate) {
		super(delegate);
	}

	@Override
	protected JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> delegate) {
		return new NodeDelegatingDeserializer(delegate);
	}

	@Override
	public Object deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		final var codec = parser.getCodec();

		final var root = codec.<ObjectNode>readTree(parser);

		final var object = root.remove("object") instanceof TextNode textNode ? textNode.asText() : "";

		var clazz = TYPES.get(object);
		if (clazz == null) {
			throw new UnsupportedOperationException("unknown object type: %s".formatted(object));
		}

		parser = codec.treeAsTokens(root);
		parser.nextToken();

		final var deserializer = context.findRootValueDeserializer(context.constructType(clazz));
		final var value = deserializer.deserialize(parser, context);

		final var isVoid = root.remove("isVoid") instanceof BooleanNode booleanNode && booleanNode.booleanValue();
		if (isVoid) {
			if (value instanceof Block block) {
				block.getChildren().clear();
			}

			if (value instanceof Inline inline) {
				inline.getChildren().clear();
			}
		}

		return value;
	}

}