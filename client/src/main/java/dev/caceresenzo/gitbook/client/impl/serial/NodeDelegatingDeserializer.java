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

import dev.caceresenzo.gitbook.model.document.Annotation;
import dev.caceresenzo.gitbook.model.document.Code;
import dev.caceresenzo.gitbook.model.document.CodeLine;
import dev.caceresenzo.gitbook.model.document.Heading1;
import dev.caceresenzo.gitbook.model.document.Heading2;
import dev.caceresenzo.gitbook.model.document.Heading3;
import dev.caceresenzo.gitbook.model.document.Image;
import dev.caceresenzo.gitbook.model.document.Images;
import dev.caceresenzo.gitbook.model.document.Link;
import dev.caceresenzo.gitbook.model.document.ListItem;
import dev.caceresenzo.gitbook.model.document.Node;
import dev.caceresenzo.gitbook.model.document.OrderedList;
import dev.caceresenzo.gitbook.model.document.Other;
import dev.caceresenzo.gitbook.model.document.Paragraph;
import dev.caceresenzo.gitbook.model.document.Table;
import dev.caceresenzo.gitbook.model.document.Text;
import dev.caceresenzo.gitbook.model.document.UnorderedList;

@SuppressWarnings("serial")
public class NodeDelegatingDeserializer extends DelegatingDeserializer {

	public static final String DISCRIMINATOR = "$class";

	private static final Map<String, Class<? extends Node>> TYPES = new HashMap<>();
	private static final Map<String, Class<? extends Node>> FALLBACKS = new HashMap<>();

	static {
		TYPES.put("block:heading-1", Heading1.class);
		TYPES.put("block:heading-2", Heading2.class);
		TYPES.put("block:heading-3", Heading3.class);
		TYPES.put("block:paragraph", Paragraph.class);
		TYPES.put("block:list-item", ListItem.class);
		TYPES.put("block:list-ordered", OrderedList.class);
		TYPES.put("block:list-unordered", UnorderedList.class);
		TYPES.put("block:table", Table.class);
		TYPES.put("block:code", Code.class);
		TYPES.put("block:code-line", CodeLine.class);
		TYPES.put("block:images", Images.class);
		TYPES.put("block:image", Image.class);
		FALLBACKS.put("block", Other.class);

		TYPES.put("text:", Text.class);

		TYPES.put("inline:annotation", Annotation.class);
		TYPES.put("inline:link", Link.class);
		//		TYPES.put("block:images", Inline.Other.class);
		//		TYPES.put("inline:inline-image", Inline.Other.class);
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
			clazz = Other.class;
			root.put("type", type);
		}

		//		System.out.println(root);

		parser = codec.treeAsTokens(root);
		parser.nextToken();

		final var deserializer = context.findRootValueDeserializer(context.constructType(clazz));
		final var value = deserializer.deserialize(parser, context);

		return value;
	}

}