package dev.caceresenzo.gitbook.model.document;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

public sealed interface Block extends Node {

	List<Node> getChildren();

	final class Code extends SimpleBlock implements Block {}

	final class CodeLine extends SimpleBlock implements Block {}

	final class Columns extends SimpleBlock implements Block {}

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

	final class Drawing extends SimpleBlock implements Block {

		@JsonProperty("data.ref")
		private Reference reference;

		@JsonProperty("fragment.caption")
		private Fragment caption;

	}

	final class Embed extends SimpleBlock implements Block {

		@JsonProperty("data.url")
		private URI url;

		@JsonProperty("fragment.caption")
		private Fragment caption;

	}

	final class Expandable extends SimpleBlock implements Block {

		@JsonProperty("fragment.expandable-title")
		private Fragment title;

		@JsonProperty("fragment.expandable-body")
		private Fragment body;

	}

	final class Heading1 extends SimpleHeading implements Block {}

	final class Heading2 extends SimpleHeading implements Block {}

	final class Heading3 extends SimpleHeading implements Block {}

	final class Hint extends SimpleHeading implements Block {

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
	@ToString(callSuper = true)
	final class Image extends SimpleBlock implements Block {

		@JsonProperty("data.alt")
		private String alt;

		@JsonProperty("data.ref")
		private Reference reference;

		@JsonProperty("fragment.caption")
		private Fragment caption;

	}

	final class ListItem extends SimpleBlock implements Block {

		@JsonProperty("data.checked")
		private Boolean checked;

	}

	final class Math extends SimpleBlock implements Block {

		@JsonProperty("data.formula")
		private String formula;

	}

	final class OrderedList extends SimpleBlock implements Block {}

	final class PageLink extends SimpleBlock implements Block {

		@JsonProperty("data.ref")
		private Reference.Page pageReference;

	}

	final class Paragraph extends SimpleBlock implements Block {}

	final class Quote extends SimpleBlock implements Block {}

	final class Stepper extends SimpleBlock implements Block {}

	final class StepperStep extends SimpleBlock implements Block {}

	@Data
	@EqualsAndHashCode(callSuper = true)
	@ToString(callSuper = true)
	final class Table extends SimpleBlock implements Block {

		@JsonProperty("records")
		private Map<String, Object> records;

	}

	final class TaskList extends SimpleBlock implements Block {}

	final class Tabs extends SimpleBlock implements Block {}

	final class Tab extends SimpleBlock implements Block {

		@JsonProperty("meta.id")
		private String id;

		@JsonProperty("data.title")
		private String title;

	}

	final class Updates extends SimpleBlock implements Block {}

	final class Update extends SimpleBlock implements Block {

		@JsonProperty("data.date")
		private LocalDate date;

	}

	final class UnorderedList extends SimpleBlock implements Block {}

	@Data
	@EqualsAndHashCode(callSuper = true)
	@ToString(callSuper = true)
	final class Other extends SimpleBlock implements Block {

		@JsonProperty("type")
		private String type;

		@JsonAnySetter
		@JsonAnyGetter
		private Map<String, Object> properties;

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

class SimpleHeading extends SimpleBlock {

	@JsonProperty("meta.id")
	private String id;

}