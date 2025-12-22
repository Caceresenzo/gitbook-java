package dev.caceresenzo.gitbook.model.document;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true, defaultImpl = Block.Other.class)
@JsonSubTypes({
	@JsonSubTypes.Type(value = Block.Code.class, name = "code"),
	@JsonSubTypes.Type(value = Block.CodeLine.class, name = "code-line"),
	@JsonSubTypes.Type(value = Block.Columns.class, name = "columns"),
	@JsonSubTypes.Type(value = Block.Column.class, name = "column"),
	@JsonSubTypes.Type(value = Block.Divider.class, name = "divider"),
	@JsonSubTypes.Type(value = Block.Drawing.class, name = "drawing"),
	@JsonSubTypes.Type(value = Block.Embed.class, name = "embed"),
	@JsonSubTypes.Type(value = Block.Expandable.class, name = "expandable"),
	@JsonSubTypes.Type(value = Block.Heading1.class, name = "heading-1"),
	@JsonSubTypes.Type(value = Block.Heading2.class, name = "heading-2"),
	@JsonSubTypes.Type(value = Block.Heading3.class, name = "heading-3"),
	@JsonSubTypes.Type(value = Block.Hint.class, name = "hint"),
	@JsonSubTypes.Type(value = Block.Images.class, name = "images"),
	@JsonSubTypes.Type(value = Block.Image.class, name = "image"),
	@JsonSubTypes.Type(value = Block.ListItem.class, name = "list-item"),
	@JsonSubTypes.Type(value = Block.Math.class, name = "math"),
	@JsonSubTypes.Type(value = Block.OrderedList.class, name = "list-ordered"),
	@JsonSubTypes.Type(value = Block.PageLink.class, name = "content-ref"),
	@JsonSubTypes.Type(value = Block.Paragraph.class, name = "paragraph"),
	@JsonSubTypes.Type(value = Block.Quote.class, name = "blockquote"),
	@JsonSubTypes.Type(value = Block.Stepper.class, name = "stepper"),
	@JsonSubTypes.Type(value = Block.StepperStep.class, name = "stepper-step"),
	@JsonSubTypes.Type(value = Block.Table.class, name = "table"),
	@JsonSubTypes.Type(value = Block.TaskList.class, name = "list-tasks"),
	@JsonSubTypes.Type(value = Block.Tabs.class, name = "tabs"),
	@JsonSubTypes.Type(value = Block.TabsItem.class, name = "tabs-item"),
	@JsonSubTypes.Type(value = Block.Updates.class, name = "updates"),
	@JsonSubTypes.Type(value = Block.Update.class, name = "update"),
	@JsonSubTypes.Type(value = Block.UnorderedList.class, name = "list-unordered"),
})
public sealed interface Block extends Node {

	List<Node> getChildren();

	default boolean hasChildren() {
		final var children = getChildren();
		return children != null && !children.isEmpty();
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Code extends SimpleBlock implements Block {

		@JsonProperty("data.syntax")
		private String syntax;

		@JsonProperty("data.lineNumbers")
		private boolean lineNumbers;

		@JsonProperty("data.title")
		private String title;

		// @JsonProperty("data.overflow")
		// private String overflow;

		@JsonProperty("data.expandable")
		private boolean expandable;

	}

	final class CodeLine extends SimpleBlock implements Block {}

	final class Columns extends SimpleBlock implements Block {}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Column extends SimpleBlock implements Block {

		@JsonProperty("data.verticalAlignment")
		private VerticalAlignment verticalAlignment;

		public enum VerticalAlignment {

			@JsonProperty("top")
			TOP,

			@JsonProperty("middle")
			MIDDLE,

			@JsonProperty("bottom")
			BOTTOM,

		}

	}

	final class Divider extends SimpleBlock implements Block {}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Drawing extends SimpleBlock implements Block {

		@JsonProperty("data.ref")
		private Reference source;

		@JsonProperty("fragment.caption")
		private Fragment caption;

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Embed extends SimpleBlock implements Block {

		@JsonProperty("data.url")
		private URI url;

		@JsonProperty("fragment.caption")
		private Fragment caption;

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Expandable extends SimpleBlock implements Block {

		@JsonProperty("meta.id")
		private String id;

		@JsonProperty("fragment.expandable-title")
		private Fragment title;

		@JsonProperty("fragment.expandable-body")
		private Fragment body;

	}

	@Getter
	@Setter
	class Heading extends SimpleBlock {

		@JsonProperty("meta.id")
		private String id;

	}

	final class Heading1 extends Heading implements Block {}

	final class Heading2 extends Heading implements Block {}

	final class Heading3 extends Heading implements Block {}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Hint extends SimpleBlock implements Block {

		@JsonProperty("data.style")
		private Style style;

		public enum Style {

			@JsonProperty("info")
			INFO,

			@JsonProperty("warning")
			WARNING,

			@JsonProperty("danger")
			DANGER,

			@JsonProperty("success")
			SUCCESS,

		}

	}

	final class Images extends SimpleBlock implements Block {}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Image extends SimpleBlock implements Block {

		@JsonProperty("data.alt")
		private String alt;

		@JsonProperty("data.ref")
		private Reference source;

		@JsonProperty("data.width")
		private Integer width;

		@JsonProperty("fragment.caption")
		private Fragment caption;

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class ListItem extends SimpleBlock implements Block {

		@JsonProperty("data.checked")
		private Boolean checked;

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Math extends SimpleBlock implements Block {

		@JsonProperty("data.formula")
		private String formula;

	}

	final class OrderedList extends SimpleBlock implements Block {}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class PageLink extends SimpleBlock implements Block {

		@JsonProperty("data.ref")
		private Reference.Page target;

	}

	final class Paragraph extends SimpleBlock implements Block {}

	final class Quote extends SimpleBlock implements Block {}

	final class Stepper extends SimpleBlock implements Block {}

	final class StepperStep extends SimpleBlock implements Block {}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Table extends SimpleBlock implements Block {

		private boolean hideHeader;

		private List<ColumnDefinition> columnDefinitions;
		private List<Row> rows;

		@Data
		public static class ColumnDefinition {

			@JsonProperty("id")
			private String id;

			@JsonProperty("title")
			private String title;

			@JsonProperty("type")
			private String type;

			@JsonProperty("textAlignment")
			private TextAlignment textAlignment;

			public enum TextAlignment {

				@JsonProperty("left")
				LEFT,

				@JsonProperty("center")
				CENTER,

				@JsonProperty("right")
				RIGHT,

			}

		}

		@Data
		public static class Row {

			private Map<String, Fragment> cells;

			public Fragment getCell(ColumnDefinition columnDefinition) {
				return cells.get(columnDefinition.getId());
			}

			public Fragment getCell(String columnDefinitionId) {
				return cells.get(columnDefinitionId);
			}

		}

	}

	final class TaskList extends SimpleBlock implements Block {}

	final class Tabs extends SimpleBlock implements Block {}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class TabsItem extends SimpleBlock implements Block {

		@JsonProperty("meta.id")
		private String id;

		@JsonProperty("data.title")
		private String title;

	}

	final class Updates extends SimpleBlock implements Block {}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Update extends SimpleBlock implements Block {

		@JsonProperty("data.date")
		private LocalDate date;

	}

	final class UnorderedList extends SimpleBlock implements Block {}

	@Data
	@EqualsAndHashCode(callSuper = true)
	final class Other extends SimpleBlock implements Block {

		@JsonProperty("type")
		private String type;

		@JsonAnySetter
		@JsonAnyGetter
		private Map<String, Object> properties;

		@Override
		public String toString() {
			return "%s(key=\"%s\", type=%s, children=%s)".formatted(getClass().getSimpleName(), getKey(), type, getChildren());
		}

	}

}

@Data
class SimpleBlock {

	@JsonProperty("key")
	private String key;

	@JsonProperty("nodes")
	private List<Node> children;

	@Override
	public String toString() {
		return "%s(key=\"%s\", children=%s)".formatted(getClass().getSimpleName(), key, children);
	}

}