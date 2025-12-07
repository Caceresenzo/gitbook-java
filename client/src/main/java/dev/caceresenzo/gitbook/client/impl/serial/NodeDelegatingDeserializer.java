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

		//		TYPES.put("block:heading-1", Block.Heading1.class);
		//		TYPES.put("block:heading-2", Block.Heading2.class);
		//		TYPES.put("block:heading-3", Block.Heading3.class);
		//		TYPES.put("block:paragraph", Block.Paragraph.class);
		//		TYPES.put("block:list-item", Block.ListItem.class);
		//		TYPES.put("block:list-ordered", Block.OrderedList.class);
		//		TYPES.put("block:list-unordered", Block.UnorderedList.class);
		//		TYPES.put("block:table", Block.Table.class);
		//		TYPES.put("block:code", Block.Code.class);
		//		TYPES.put("block:code-line", Block.CodeLine.class);
		//		TYPES.put("block:images", Block.Images.class);
		//		TYPES.put("block:image", Block.Image.class);
		//		TYPES.put("block:divider", Block.Divider.class);
		//		TYPES.put("block:embed", Block.Embed.class);
		//		TYPES.put("block:hint", Block.Hint.class);
		//		TYPES.put("block:math", Block.Math.class);
		//		TYPES.put("block:expandable", Block.Expandable.class);
		//		TYPES.put("block:list-tasks", Block.TaskList.class);
		//		TYPES.put("block:blockquote", Block.Quote.class);
		//		TYPES.put("block:tabs", Block.Tabs.class);
		//		TYPES.put("block:tabs-item", Block.Tab.class);
		//		TYPES.put("block:stepper", Block.Stepper.class);
		//		TYPES.put("block:stepper-step", Block.StepperStep.class);
		//		TYPES.put("block:updates", Block.Updates.class);
		//		TYPES.put("block:update", Block.Update.class);
		//		TYPES.put("block:drawing", Block.Drawing.class);
		//		TYPES.put("block:content-ref", Block.PageLink.class);
		//		TYPES.put("block:columns", Block.Columns.class);
		//		TYPES.put("block:column", Block.Column.class);
		//		FALLBACKS.put("block", Block.Other.class);
		//
		//		TYPES.put("text:", Text.class);
		//
		//		TYPES.put("inline:annotation", Inline.Annotation.class);
		//		TYPES.put("inline:link", Inline.Link.class);
		//		TYPES.put("inline:mention", Inline.Mention.class);
		//		TYPES.put("inline:inline-math", Inline.Math.class);
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

		var clazz = TYPES.get(object);
		if (clazz == null) {
			throw new UnsupportedOperationException("unknown object type: %s".formatted(object));
		}

		parser = codec.treeAsTokens(root);
		parser.nextToken();

		final var deserializer = context.findRootValueDeserializer(context.constructType(clazz));
		final var value = deserializer.deserialize(parser, context);

		if (value instanceof Block block) {
			final var isVoid = root.remove("isVoid") instanceof BooleanNode booleanNode && booleanNode.booleanValue();

			if (isVoid) {
				block.getChildren().clear();
			}
		}

		return value;
	}

}