package dev.caceresenzo.gitbook.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true, defaultImpl = SiteStructure.Other.class)
@JsonSubTypes({
	@JsonSubTypes.Type(value = SiteStructure.Sections.class, name = "sections"),
	@JsonSubTypes.Type(value = SiteStructure.SiteSpaces.class, name = "siteSpaces"),
})
@Data
public abstract sealed class SiteStructure {

	@Data
	@EqualsAndHashCode(callSuper = true)
	@ToString(callSuper = true)
	public static final class Sections extends SiteStructure {

		@JsonProperty("structure")
		private List<Structure> structure;

		@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "object", visible = true, defaultImpl = Structure.Other.class)
		@JsonSubTypes({
			@JsonSubTypes.Type(value = Structure.Section.class, name = "site-section"),
			@JsonSubTypes.Type(value = Structure.SectionGroup.class, name = "site-section-group"),
		})
		@Data
		public abstract static sealed class Structure {

			@JsonProperty("id")
			private String id;

			@JsonProperty("title")
			private String title;

			@JsonProperty("sectionGroup")
			private String sectionGroupId;

			@JsonProperty("icon")
			private String icon;

			@Data
			@EqualsAndHashCode(callSuper = true)
			@ToString(callSuper = true)
			public static final class Section extends Structure {

				@JsonProperty("description")
				private String description;

				@JsonProperty("default")
				private boolean isDefault;

				@JsonProperty("path")
				private String path;

				@JsonProperty("siteSpaces")
				private List<SiteStructure.SiteSpaces.Structure.SiteSpace> siteSpaces;

				@JsonProperty("urls")
				private Urls urls;

				@Data
				public static class Urls {

					@JsonProperty("published")
					private String published;

				}

			}

			@Data
			@EqualsAndHashCode(callSuper = true)
			@ToString(callSuper = true)
			public static final class SectionGroup extends Structure {

				@JsonProperty("children")
				private List<Structure> children;

			}

			@Data
			@EqualsAndHashCode(callSuper = true)
			@ToString(callSuper = true)
			public static final class Other extends Structure {

				@JsonProperty("object")
				private String object;

				@JsonAnySetter
				@JsonAnyGetter
				@JsonIgnore
				private Map<String, Object> properties;

			}

		}

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	@ToString(callSuper = true)
	public static final class SiteSpaces extends SiteStructure {

		@JsonProperty("structure")
		private List<Structure> structure;

		@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "object", visible = true, defaultImpl = Structure.Other.class)
		@JsonSubTypes({
			@JsonSubTypes.Type(value = Structure.SiteSpace.class, name = "site-space"),
		})
		@Data
		public abstract static sealed class Structure {

			@JsonProperty("id")
			private String id;

			@JsonProperty("title")
			private String title;

			@Data
			@EqualsAndHashCode(callSuper = true)
			@ToString(callSuper = true)
			public static final class SiteSpace extends Structure {

				@JsonProperty("path")
				private String path;

				@JsonProperty("section")
				private String sectionId;

				@JsonProperty("space")
				private Space space;

				@JsonProperty("default")
				public boolean isDefault;

				@JsonProperty("hidden")
				public boolean hidden;

				@JsonProperty("urls")
				private Urls urls;

				@Data
				public static class Urls {

					@JsonProperty("published")
					private String published;

				}

			}

			@Data
			@EqualsAndHashCode(callSuper = true)
			@ToString(callSuper = true)
			public static final class Other extends Structure {

				@JsonProperty("object")
				private String object;

				@JsonAnySetter
				@JsonAnyGetter
				@JsonIgnore
				private Map<String, Object> properties;

			}

		}

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	@ToString(callSuper = true)
	public static final class Other extends SiteStructure {

		@JsonProperty("type")
		private String type;

		@JsonAnySetter
		@JsonAnyGetter
		@JsonIgnore
		private Map<String, Object> properties;

	}

}