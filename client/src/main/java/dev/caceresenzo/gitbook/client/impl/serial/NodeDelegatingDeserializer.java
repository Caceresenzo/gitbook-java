package dev.caceresenzo.gitbook.client.impl.serial;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import dev.caceresenzo.gitbook.model.document.Block;
import dev.caceresenzo.gitbook.model.document.Inline;
import dev.caceresenzo.gitbook.model.document.Node;

@SuppressWarnings("serial")
public class NodeDelegatingDeserializer extends DelegatingDeserializer {

	public static final String DISCRIMINATOR = "$class";

	private static final Map<String, Class<?>> TYPES = new HashMap<>();

	static {
		TYPES.put("block:heading-1", Block.Heading1.class);
		TYPES.put("block:heading-2", Block.Heading2.class);
		TYPES.put("block:heading-3", Block.Heading3.class);
		TYPES.put("block:paragraph", Block.Paragraph.class);
		TYPES.put("block:list-item", Block.ListItem.class);
		TYPES.put("block:list-ordered", Block.OrderedList.class);
		TYPES.put("block:list-unordered", Block.UnorderedList.class);
		TYPES.put("block:table", Block.Table.class);

		TYPES.put("text:", Node.Text.class);

		TYPES.put("inline:link", Inline.Link.class);
		TYPES.put("block:images", Inline.Other.class);
		TYPES.put("inline:inline-image", Inline.Other.class);
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
		final var type = root.remove("type") instanceof TextNode textNode ? textNode.asText() : "";
		final var discriminator = "%s:%s".formatted(object, type);

		root.put("key", root.get("key").asText() + "  " + discriminator);

		var clazz = TYPES.get(discriminator);
		if (clazz == null) {
			System.out.println(discriminator);
			return null;
		}

		parser = codec.treeAsTokens(root);
		parser.nextToken();

		final var deserializer = context.findRootValueDeserializer(context.constructType(clazz));
		final var value = deserializer.deserialize(parser, context);

		if (value instanceof Block.Table table) {
			System.out.println(table);
		} else {
			System.out.println(value.getClass());
		}

		return value;
	}

}