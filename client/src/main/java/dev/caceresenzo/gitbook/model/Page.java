package dev.caceresenzo.gitbook.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import dev.caceresenzo.gitbook.model.document.Reference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true, defaultImpl = Page.Other.class)
@JsonSubTypes({
	@JsonSubTypes.Type(value = Page.Document.class, name = "document"),
	@JsonSubTypes.Type(value = Page.Group.class, name = "group"),
	@JsonSubTypes.Type(value = Page.Link.class, name = "link"),
})
@Data
public abstract sealed class Page {

	@JsonProperty("id")
	private String id;

	@JsonProperty("title")
	private String title;

	@JsonProperty("emoji")
	private String emoji;

	@JsonProperty("createdAt")
	private Date createdAt;

	@JsonProperty("updatedAt")
	private Date updatedAt;

	@Data
	@EqualsAndHashCode(callSuper = true)
	@ToString(callSuper = true)
	public static final class Document extends Page {

		@JsonProperty("slug")
		private String slug;

		@JsonProperty("path")
		private String path;

		@JsonProperty("description")
		private String description;

		@JsonProperty("pages")
		private List<Page> children;

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	@ToString(callSuper = true)
	public static final class Group extends Page {

		@JsonProperty("slug")
		private String slug;

		@JsonProperty("path")
		private String path;

		@JsonProperty("pages")
		private List<Page> children;

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	@ToString(callSuper = true)
	public static final class Link extends Page {

		@JsonProperty("target")
		private Reference target;

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	@ToString(callSuper = true)
	public static final class Other extends Page {

		@JsonProperty("type")
		private String type;

		@JsonAnySetter
		@JsonAnyGetter
		@JsonIgnore
		private Map<String, Object> properties;

	}

}