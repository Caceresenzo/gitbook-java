package dev.caceresenzo.gitbook.model.document;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import dev.caceresenzo.gitbook.BaseGitBookTest;
import dev.caceresenzo.gitbook.model.Page;
import dev.caceresenzo.gitbook.model.document.Block.Paragraph;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class NodeTest extends BaseGitBookTest {

	public static final String SPACE_ID_ENV_VAR = "GITBOOK_COMPONENTS_SPACE_ID";
	public static final String LOREM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

	static String spaceId;

	@BeforeAll
	static void setUp() {
		spaceId = assertEnvironmentVariable(SPACE_ID_ENV_VAR);
	}

	@Nested
	@Order(10)
	@DisplayName("Simple")
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	class SimpleGroup {

		@Order(10)
		@DisplayName("Text")
		@Test
		void testText() {
			final var nodes = getNodes("/simple/text");
			assertThat(nodes).hasSize(4);

			{
				final var paragraph = assertInstanceOf(Block.Paragraph.class, nodes.get(0));

				assertContainsSingleTextContent(LOREM, paragraph);
			}

			{
				final var heading = assertInstanceOf(Block.Heading1.class, nodes.get(1));

				assertEquals("heading-1", heading.getId());
				assertContainsSingleTextContent("Heading 1", heading);
			}

			{
				final var heading = assertInstanceOf(Block.Heading2.class, nodes.get(2));

				assertEquals("heading-2", heading.getId());
				assertContainsSingleTextContent("Heading 2", heading);
			}

			{
				final var heading = assertInstanceOf(Block.Heading3.class, nodes.get(3));

				assertEquals("heading-3", heading.getId());
				assertContainsSingleTextContent("Heading 3", heading);
			}
		}

		@Order(10)
		@DisplayName("List")
		@Test
		void testList() {
			final var nodes = getNodes("/simple/list");
			assertThat(nodes).hasSize(6);

			{
				final var heading = assertInstanceOf(Block.Heading1.class, nodes.get(0));
				assertContainsSingleTextContent("Unordered", heading);

				final var list = assertInstanceOf(Block.UnorderedList.class, nodes.get(1));

				final var listItems = list.getChildren();
				assertThat(listItems).hasSize(2);

				final var firstItem = assertInstanceOf(Block.ListItem.class, listItems.get(0));
				assertNull(firstItem.getChecked());
				assertContainsSingleParagraphWithTextContent("Item #1", firstItem);

				final var secondItem = assertInstanceOf(Block.ListItem.class, listItems.get(1));
				assertNull(secondItem.getChecked());
				assertContainsSingleParagraphWithTextContent("Item #2", secondItem);
			}

			{
				final var heading = assertInstanceOf(Block.Heading1.class, nodes.get(2));
				assertContainsSingleTextContent("Ordered", heading);

				final var list = assertInstanceOf(Block.OrderedList.class, nodes.get(3));

				final var listItems = list.getChildren();
				assertThat(listItems).hasSize(2);

				final var firstItem = assertInstanceOf(Block.ListItem.class, listItems.get(0));
				assertNull(firstItem.getChecked());
				assertContainsSingleParagraphWithTextContent("Item #1", firstItem);

				final var secondItem = assertInstanceOf(Block.ListItem.class, listItems.get(1));
				assertNull(secondItem.getChecked());
				assertContainsSingleParagraphWithTextContent("Item #2", secondItem);
			}

			{
				final var heading = assertInstanceOf(Block.Heading1.class, nodes.get(4));
				assertContainsSingleTextContent("Task", heading);

				final var list = assertInstanceOf(Block.TaskList.class, nodes.get(5));

				final var listItems = list.getChildren();
				assertThat(listItems).hasSize(3);

				final var firstItem = assertInstanceOf(Block.ListItem.class, listItems.get(0));
				assertEquals(Boolean.FALSE, firstItem.getChecked());
				assertContainsSingleParagraphWithTextContent("Item #1", firstItem);

				final var secondItem = assertInstanceOf(Block.ListItem.class, listItems.get(1));
				assertEquals(Boolean.FALSE, secondItem.getChecked());
				assertContainsSingleParagraphWithTextContent("Item #2", secondItem);

				final var thirdItem = assertInstanceOf(Block.ListItem.class, listItems.get(2));
				assertEquals(Boolean.TRUE, thirdItem.getChecked());
				assertContainsSingleParagraphWithTextContent("Checked", thirdItem);
			}
		}

		@Order(20)
		@DisplayName("Hint")
		@Test
		void testHint() {
			final var nodes = getNodes("/simple/hint");
			assertThat(nodes).hasSize(4);

			{
				final var hint = assertInstanceOf(Block.Hint.class, nodes.get(0));
				assertEquals(Block.Hint.Style.INFO, hint.getStyle());

				assertContainsSingleParagraphWithTextContent(LOREM, hint);
			}

			{
				final var hint = assertInstanceOf(Block.Hint.class, nodes.get(1));
				assertEquals(Block.Hint.Style.WARNING, hint.getStyle());

				assertContainsSingleParagraphWithTextContent(LOREM, hint);
			}

			{
				final var hint = assertInstanceOf(Block.Hint.class, nodes.get(2));
				assertEquals(Block.Hint.Style.DANGER, hint.getStyle());

				assertContainsSingleParagraphWithTextContent(LOREM, hint);
			}

			{
				final var hint = assertInstanceOf(Block.Hint.class, nodes.get(3));
				assertEquals(Block.Hint.Style.SUCCESS, hint.getStyle());

				assertContainsSingleParagraphWithTextContent(LOREM, hint);
			}
		}

		@Order(30)
		@DisplayName("Quote")
		@Test
		void testQuote() {
			final var nodes = getNodes("/simple/quote");
			assertThat(nodes).hasSize(1);

			{
				final var hint = assertInstanceOf(Block.Quote.class, nodes.get(0));

				assertContainsSingleParagraphWithTextContent(LOREM, hint);
			}
		}

		@Order(40)
		@DisplayName("Divider")
		@Test
		void testDivider() {
			final var nodes = getNodes("/simple/divider");
			assertThat(nodes).hasSize(3);

			assertInstanceOf(Block.Paragraph.class, nodes.get(0));
			assertInstanceOf(Block.Divider.class, nodes.get(1));
			assertInstanceOf(Block.Paragraph.class, nodes.get(2));
		}

		@Order(50)
		@DisplayName("Code Block")
		@Test
		void testCodeBlock() {
			final var nodes = getNodes("/simple/code-block");
			assertThat(nodes).hasSize(4);

			{
				final var heading = assertInstanceOf(Block.Heading1.class, nodes.get(0));
				assertContainsSingleTextContent("Regular", heading);

				final var code = assertInstanceOf(Block.Code.class, nodes.get(1));
				assertEquals("python", code.getSyntax());

				final var lines = code.getChildren();
				assertThat(lines).hasSize(1);

				final var line = assertInstanceOf(Block.CodeLine.class, lines.get(0));
				assertContainsSingleTextContent("print(42)", line);
			}

			{
				final var heading = assertInstanceOf(Block.Heading1.class, nodes.get(2));
				assertContainsSingleTextContent("With options", heading);

				final var code = assertInstanceOf(Block.Code.class, nodes.get(3));
				assertEquals("python", code.getSyntax());
				assertTrue(code.isLineNumbers());
				assertEquals("hello.py", code.getTitle());
				assertTrue(code.isExpandable());

				final var lines = code.getChildren();
				assertThat(lines).hasSize(1);

				final var line = assertInstanceOf(Block.CodeLine.class, lines.get(0));
				assertContainsSingleTextContent("print(42)", line);
			}
		}

	}

	@Nested
	@Order(20)
	@DisplayName("Advanced")
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	class AdvancedGroup {

		@Order(10)
		@DisplayName("Image")
		@Test
		void testImage() {
			final var nodes = getNodes("/advanced/image");
			assertThat(nodes).hasSize(1);

			final var images = assertInstanceOf(Block.Images.class, nodes.get(0));
			assertThat(images.getChildren()).hasSize(1);

			final var image = assertInstanceOf(Block.Image.class, images.getChildren().get(0));
			assertThat(image.getChildren()).isEmpty();

			assertInstanceOf(Reference.Url.class, image.getSource());
			assertEquals("An alt text.", image.getAlt());

			final var caption = image.getCaption();
			assertNotNull(caption);
			assertIsSingleParagraphWithTextContent("A caption.", caption.getNodes());
		}

		@Order(20)
		@DisplayName("Embed")
		@Test
		void testEmbed() {
			final var nodes = getNodes("/advanced/embed");
			assertThat(nodes).hasSize(4);

			{
				assertInstanceOf(Block.Heading1.class, nodes.get(0));

				final var embed = assertInstanceOf(Block.Embed.class, nodes.get(1));
				assertEquals(URI.create("http://www.monip.org/"), embed.getUrl());

				final var caption = embed.getCaption();
				assertNotNull(caption);
				assertIsSingleParagraphWithTextContent("A caption.", caption.getNodes());
			}

			{
				assertInstanceOf(Block.Heading1.class, nodes.get(2));

				final var embed = assertInstanceOf(Block.Embed.class, nodes.get(3));
				assertEquals(URI.create("https://www.rd.usda.gov/sites/default/files/pdf-sample_0.pdf"), embed.getUrl());

				final var caption = embed.getCaption();
				assertNotNull(caption);
				assertIsSingleParagraphWithTextContent("A caption.", caption.getNodes());
			}
		}

		@Order(30)
		@DisplayName("Table")
		@Test
		void testTable() {
			final var nodes = getNodes("/advanced/table");
			assertThat(nodes).hasSize(4);

			{
				assertInstanceOf(Block.Heading1.class, nodes.get(0));

				final var table = assertInstanceOf(Block.Table.class, nodes.get(1));

				final var columnDefinitions = table.getColumnDefinitions();
				assertThat(columnDefinitions)
					.hasSize(3)
					.satisfiesExactly(
						(definition) -> {
							assertEquals("Header 1", definition.getTitle());
							assertEquals(Block.Table.ColumnDefinition.TextAlignment.LEFT, definition.getTextAlignment());
						},
						(definition) -> {
							assertEquals("Header 2", definition.getTitle());
							assertEquals(Block.Table.ColumnDefinition.TextAlignment.CENTER, definition.getTextAlignment());
						},
						(definition) -> {
							assertEquals("Header 3", definition.getTitle());
							assertEquals(Block.Table.ColumnDefinition.TextAlignment.RIGHT, definition.getTextAlignment());
						}
					);

				final var left = columnDefinitions.get(0);
				final var middle = columnDefinitions.get(1);
				final var right = columnDefinitions.get(2);

				final var rows = table.getRows();
				assertThat(rows)
					.hasSize(3)
					.satisfiesExactly(
						(row) -> {
							assertIsSingleParagraphWithTextContent("Line 1, 1", row.getCell(left).getNodes());
							assertIsSingleParagraphWithTextContent("Line 1, 2", row.getCell(middle).getNodes());
							assertIsSingleParagraphWithTextContent("Line 1, 3", row.getCell(right).getNodes());
						},
						(row) -> {
							assertIsSingleParagraphWithTextContent("Line 2, 1", row.getCell(left).getNodes());
							assertIsSingleParagraphWithTextContent("Line 2, 2", row.getCell(middle).getNodes());
							assertIsSingleParagraphWithTextContent("Line 2, 3", row.getCell(right).getNodes());
						},
						(row) -> {
							assertIsSingleParagraphWithTextContent("Line 3, 1", row.getCell(left).getNodes());
							assertIsSingleParagraphWithTextContent("Line 3, 2", row.getCell(middle).getNodes());
							assertIsSingleParagraphWithTextContent("Line 3, 3", row.getCell(right).getNodes());
						}
					);
			}
		}

		@Order(40)
		@DisplayName("Cards")
		@Test
		void testCards() {
			final var nodes = getNodes("/advanced/cards");
			assertThat(nodes).hasSize(1);

			// TODO
		}

		@Order(50)
		@DisplayName("Tabs")
		@Test
		void testTabs() {
			final var nodes = getNodes("/advanced/tabs");
			assertThat(nodes).hasSize(1);

			final var tabs = assertInstanceOf(Block.Tabs.class, nodes.get(0));
			final var tabsItems = tabs.getChildren();
			assertThat(tabsItems).hasSize(2);

			{
				final var tab = assertInstanceOf(Block.TabsItem.class, tabsItems.get(0));
				assertEquals("first-tab", tab.getId());
				assertEquals("First Tab", tab.getTitle());

				assertIsSingleParagraphWithTextContent("First Tab Content", tab.getChildren());
			}

			{
				final var tab = assertInstanceOf(Block.TabsItem.class, tabsItems.get(1));
				assertEquals("second-tab", tab.getId());
				assertEquals("Second Tab", tab.getTitle());

				assertIsSingleParagraphWithTextContent("Second Tab Content", tab.getChildren());
			}
		}

		@Order(60)
		@DisplayName("Expandable")
		@Test
		void testExpandable() {
			final var nodes = getNodes("/advanced/expandable");
			assertThat(nodes).hasSize(2);

			{
				final var expandable = assertInstanceOf(Block.Expandable.class, nodes.get(0));
				assertEquals("first-expandable", expandable.getId());

				final var title = expandable.getTitle();
				assertNotNull(title);
				assertIsSingleParagraphWithTextContent("First Expandable", title.getNodes());

				final var body = expandable.getBody();
				assertNotNull(body);
				assertIsSingleParagraphWithTextContent("First Expandable Content", body.getNodes());
			}

			{
				final var expandable = assertInstanceOf(Block.Expandable.class, nodes.get(1));
				assertEquals("second-expandable", expandable.getId());

				final var title = expandable.getTitle();
				assertNotNull(title);
				assertIsSingleParagraphWithTextContent("Second Expandable", title.getNodes());

				final var body = expandable.getBody();
				assertNotNull(body);
				assertIsSingleParagraphWithTextContent("Second Expandable Content", body.getNodes());
			}
		}

		@Order(70)
		@DisplayName("Stepper")
		@Test
		void testStepper() {
			final var nodes = getNodes("/advanced/stepper");
			assertThat(nodes).hasSize(1);

			final var root = assertInstanceOf(Block.Stepper.class, nodes.get(0));

			final var steps = root.getChildren();
			assertThat(steps).hasSize(3);

			{
				final var step = assertInstanceOf(Block.StepperStep.class, steps.get(0));

				final var children = step.getChildren();
				assertThat(children).hasSize(2);

				final var title = assertInstanceOf(Block.Heading2.class, children.get(0));
				assertEquals("first-step", title.getId());
				assertContainsSingleTextContent("First Step", title);

				final var content = assertInstanceOf(Block.Paragraph.class, children.get(1));
				assertContainsSingleTextContent("First Step Content", content);
			}

			{
				final var step = assertInstanceOf(Block.StepperStep.class, steps.get(1));

				final var children = step.getChildren();
				assertThat(children).hasSize(2);

				final var title = assertInstanceOf(Block.Heading2.class, children.get(0));
				assertEquals("second-step", title.getId());
				assertContainsSingleTextContent("Second Step", title);

				final var content = assertInstanceOf(Block.Paragraph.class, children.get(1));
				assertContainsSingleTextContent("Second Step Content", content);
			}

			{
				final var step = assertInstanceOf(Block.StepperStep.class, steps.get(2));

				final var children = step.getChildren();
				assertThat(children).hasSize(2);

				final var title = assertInstanceOf(Block.Heading2.class, children.get(0));
				assertEquals("third-step", title.getId());
				assertContainsSingleTextContent("Third Step", title);

				final var content = assertInstanceOf(Block.Paragraph.class, children.get(1));
				assertContainsSingleTextContent("Third Step Content", content);
			}
		}

		@Order(80)
		@DisplayName("Updates")
		@Test
		void testUpdates() {
			final var nodes = getNodes("/advanced/update");
			assertThat(nodes).hasSize(1);

			final var root = assertInstanceOf(Block.Updates.class, nodes.get(0));

			final var updates = root.getChildren();
			assertThat(updates).hasSize(2);

			{
				final var update = assertInstanceOf(Block.Update.class, updates.get(0));

				final var children = update.getChildren();
				assertThat(children).hasSize(2);

				final var title = assertInstanceOf(Block.Heading1.class, children.get(0));
				assertEquals("first-update", title.getId());
				assertContainsSingleTextContent("First Update", title);

				final var content = assertInstanceOf(Block.Paragraph.class, children.get(1));
				assertContainsSingleTextContent("First Update Content", content);
			}

			{
				final var update = assertInstanceOf(Block.Update.class, updates.get(1));

				final var children = update.getChildren();
				assertThat(children).hasSize(2);

				final var title = assertInstanceOf(Block.Heading1.class, children.get(0));
				assertEquals("second-update", title.getId());
				assertContainsSingleTextContent("Second Update", title);

				final var content = assertInstanceOf(Block.Paragraph.class, children.get(1));
				assertContainsSingleTextContent("Second Update Content", content);
			}
		}

		@Order(90)
		@DisplayName("Drawing")
		@Test
		void testDrawing() {
			final var nodes = getNodes("/advanced/drawing");
			assertThat(nodes).hasSize(1);

			final var drawing = assertInstanceOf(Block.Drawing.class, nodes.get(0));
			assertInstanceOf(Reference.File.class, drawing.getSource());
			assertThat(drawing.getChildren()).isEmpty();

			final var caption = drawing.getCaption();
			assertNotNull(caption);
			assertIsSingleParagraphWithTextContent("A caption.", caption.getNodes());
		}

		@Order(100)
		@DisplayName("Math")
		@Test
		void testMath() {
			final var nodes = getNodes("/advanced/math");
			assertThat(nodes).hasSize(1);

			final var math = assertInstanceOf(Block.Math.class, nodes.get(0));
			assertEquals("f(x) = x * e^{2 pi i \\xi x}", math.getFormula());
			assertThat(math.getChildren()).isEmpty();
		}

		@Order(110)
		@DisplayName("PageLink")
		@Test
		void testPageLink() {
			final var nodes = getNodes("/advanced/page-link");
			assertThat(nodes).hasSize(2);

			final var pages = client.getSpacePages(spaceId)
				.orElseThrow();

			{
				final var textPageId = pages.stream()
					.filter((page) -> page instanceof Page.Group group && "Simple".equalsIgnoreCase(group.getTitle()))
					.map(Page.Group.class::cast)
					.findFirst().orElseThrow()
					.getChildren().stream()
					.filter((page) -> "Text".equalsIgnoreCase(page.getTitle()))
					.findFirst().orElseThrow()
					.getId();

				final var pageLink = assertInstanceOf(Block.PageLink.class, nodes.get(0));
				assertEquals(textPageId, pageLink.getTarget().id());
				assertThat(pageLink.getChildren()).isEmpty();
			}

			{
				final var imagePageId = pages.stream()
					.filter((page) -> page instanceof Page.Group group && "Advanced".equalsIgnoreCase(group.getTitle()))
					.map(Page.Group.class::cast)
					.findFirst().orElseThrow()
					.getChildren().stream()
					.filter((page) -> "Image".equalsIgnoreCase(page.getTitle()))
					.findFirst().orElseThrow()
					.getId();

				final var pageLink = assertInstanceOf(Block.PageLink.class, nodes.get(1));
				assertEquals(imagePageId, pageLink.getTarget().id());
				assertThat(pageLink.getChildren()).isEmpty();
			}
		}

		@Order(120)
		@DisplayName("Column")
		@Test
		void testColumn() {
			final var nodes = getNodes("/advanced/column");
			assertThat(nodes).hasSize(1);

			final var root = assertInstanceOf(Block.Columns.class, nodes.get(0));

			final var columns = root.getChildren();
			assertThat(columns).hasSize(2);

			{
				final var column = assertInstanceOf(Block.Column.class, columns.get(0));
				assertContainsSingleParagraphWithTextContent("Hello World", column);
			}

			{
				final var column = assertInstanceOf(Block.Column.class, columns.get(1));

				final var children = column.getChildren();
				assertThat(children).hasSize(1);

				final var hint = assertInstanceOf(Block.Hint.class, children.get(0));
				assertEquals(Block.Hint.Style.INFO, hint.getStyle());

				final var hintChildren = hint.getChildren();
				assertIsSingleParagraphWithTextContent("From another column", hintChildren);
			}
		}

	}

	@Nested
	@Order(30)
	@DisplayName("Inline")
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	class InlineGroup {

		@Nested
		@Order(10)
		@DisplayName("Simple")
		@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
		@TestInstance(Lifecycle.PER_CLASS)
		class SimpleGroup {

			List<Node> nodes;

			@BeforeAll
			void setUp() {
				nodes = getNodes("/inline/simple");
			}

			@Order(10)
			@DisplayName("Bold")
			@Test
			void testBold() {
				doTest(
					0, "Bold",
					1, "Lorem ",
					"ipsum dolor sit amet", new Mark.Bold(),
					", consectetur adipiscing elit."
				);
			}

			@Order(20)
			@DisplayName("Italic")
			@Test
			void testItalic() {
				doTest(
					2, "Italic",
					3, "Lorem ",
					"ipsum dolor sit amet", new Mark.Italic(),
					", consectetur adipiscing elit."
				);

			}

			@Order(30)
			@DisplayName("Strikethrough")
			@Test
			void testStrikethrough() {
				doTest(
					4, "Strikethrough",
					5, "Lorem ",
					"ipsum dolor sit amet", new Mark.Strikethrough(),
					", consectetur adipiscing elit."
				);
			}

			@Order(40)
			@DisplayName("Superscript")
			@Test
			void testSuperscript() {
				doTest(
					6, "Superscript",
					7, "Lorem ",
					"ipsum dolor sit amet", new Mark.Superscript(),
					", consectetur adipiscing elit."
				);
			}

			@Order(50)
			@DisplayName("Subscript")
			@Test
			void testSubscript() {
				doTest(
					8, "Subscript",
					9, "Lorem ",
					"ipsum dolor sit amet", new Mark.Subscript(),
					", consectetur adipiscing elit."
				);
			}

			@Order(60)
			@DisplayName("Code")
			@Test
			void testCode() {
				doTest(
					10, "Code",
					11, "Lorem ",
					"ipsum dolor sit amet", new Mark.Code(),
					", consectetur adipiscing elit."
				);
			}

			@Order(70)
			@DisplayName("Keyboard")
			@Test
			void testKeyboard() {
				doTest(
					12, "Keyboard",
					13, "Lorem ",
					"ipsum dolor sit amet", new Mark.Keyboard(),
					", consectetur adipiscing elit."
				);
			}

			@Order(80)
			@DisplayName("Link")
			@Test
			void testLink() {
				final var title = assertInstanceOf(Block.Heading1.class, nodes.get(14));
				assertContainsSingleTextContent("Link", title);

				final var content = assertInstanceOf(Block.Paragraph.class, nodes.get(15));

				final var children = content.getChildren();
				assertThat(children).hasSize(3);

				final var prefix = assertInstanceOf(Text.class, children.get(0));
				assertThat(prefix.getLeaves()).containsExactly(leaf("Lorem "));

				final var text = assertInstanceOf(Inline.Link.class, children.get(1));
				assertContainsSingleTextContent("ipsum dolor sit amet", text);
				assertEquals(new Reference.Url("http://www.monip.org/"), text.getReference());

				final var suffix = assertInstanceOf(Text.class, children.get(2));
				assertThat(suffix.getLeaves()).containsExactly(leaf(", consectetur adipiscing elit."));
			}

			@Order(90)
			@DisplayName("Annotate")
			@Test
			void testAnnotate() {
				final var title = assertInstanceOf(Block.Heading1.class, nodes.get(16));
				assertContainsSingleTextContent("Annotate", title);

				final var content = assertInstanceOf(Block.Paragraph.class, nodes.get(17));

				final var children = content.getChildren();
				assertThat(children).hasSize(3);

				final var prefix = assertInstanceOf(Text.class, children.get(0));
				assertThat(prefix.getLeaves()).containsExactly(leaf("Lorem "));

				final var text = assertInstanceOf(Inline.Annotation.class, children.get(1));
				assertContainsSingleTextContent("ipsum dolor sit amet", text);

				final var body = text.getBody();
				assertNotNull(body);
				assertIsSingleParagraphWithTextContent("Hello World!", body.getNodes());

				final var suffix = assertInstanceOf(Text.class, children.get(2));
				assertThat(suffix.getLeaves()).containsExactly(leaf(", consectetur adipiscing elit."));
			}

			@Order(100)
			@DisplayName("Color")
			@Test
			void testColor() {
				final var title = assertInstanceOf(Block.Heading1.class, nodes.get(18));
				assertContainsSingleTextContent("Color", title);

				final var content = assertInstanceOf(Block.Paragraph.class, nodes.get(19));
				final var text = assertInstanceOf(Text.class, content.getChildren().get(0));
				assertThat(text.getLeaves())
					.containsExactly(
						leaf("Lorem "),
						leaf("ipsum dolor sit amet", new Mark.Color("default", "$success")),
						leaf(", "),
						leaf("consectetur adipiscing elit", new Mark.Color("purple", "default")),
						leaf(".")
					);
			}

			@Order(110)
			@DisplayName("Math")
			@Test
			void testMath() {
				final var title = assertInstanceOf(Block.Heading1.class, nodes.get(20));
				assertContainsSingleTextContent("Math", title);

				final var content = assertInstanceOf(Block.Paragraph.class, nodes.get(21));

				final var text = assertInstanceOf(Text.class, content.getChildren().get(0));
				assertThat(text.getLeaves())
					.containsExactly(
						leaf("Lorem ipsum dolor sit amet, consectetur adipiscing elit. ")
					);

				final var math = assertInstanceOf(Inline.Math.class, content.getChildren().get(1));
				assertEquals("f(x) = x * e^{2 pi i \\xi x}", math.getFormula());
				assertThat(math.getChildren()).isEmpty();

				final var suffix = assertInstanceOf(Text.class, content.getChildren().get(2));
				assertThat(suffix.getLeaves())
					.containsExactly(
						leaf("")
					);
			}

			@Order(120)
			@DisplayName("Button")
			@Test
			void testButton() {
				final var title = assertInstanceOf(Block.Heading1.class, nodes.get(22));
				assertContainsSingleTextContent("Button", title);

				final var content = assertInstanceOf(Block.Paragraph.class, nodes.get(23));

				final var text = assertInstanceOf(Text.class, content.getChildren().get(0));
				assertThat(text.getLeaves())
					.containsExactly(
						leaf("Lorem ipsum dolor sit amet, consectetur adipiscing elit. ")
					);

				final var button = assertInstanceOf(Inline.Button.class, content.getChildren().get(1));
				assertNotNull(button.getReference());
				assertEquals("A button", button.getLabel());
				assertEquals(Inline.Button.Kind.PRIMARY, button.getKind());
				assertEquals("1", button.getIcon());

				final var suffix = assertInstanceOf(Text.class, content.getChildren().get(2));
				assertThat(suffix.getLeaves())
					.containsExactly(
						leaf("")
					);
			}

			@Order(130)
			@DisplayName("Emoji")
			@Test
			void testEmoji() {
				final var title = assertInstanceOf(Block.Heading1.class, nodes.get(24));
				assertContainsSingleTextContent("Emoji", title);

				final var content = assertInstanceOf(Block.Paragraph.class, nodes.get(25));

				final var text = assertInstanceOf(Text.class, content.getChildren().get(0));
				assertThat(text.getLeaves())
					.containsExactly(
						leaf("Lorem ipsum dolor sit amet, consectetur adipiscing elit. ")
					);

				final var emoji = assertInstanceOf(Inline.Emoji.class, content.getChildren().get(1));
				assertEquals("1f44d", emoji.getCode());

				final var suffix = assertInstanceOf(Text.class, content.getChildren().get(2));
				assertThat(suffix.getLeaves())
					.containsExactly(
						leaf("")
					);
			}

			@Order(140)
			@DisplayName("Icon")
			@Test
			void testIcon() {
				final var title = assertInstanceOf(Block.Heading1.class, nodes.get(26));
				assertContainsSingleTextContent("Icon", title);

				final var content = assertInstanceOf(Block.Paragraph.class, nodes.get(27));

				final var text = assertInstanceOf(Text.class, content.getChildren().get(0));
				assertThat(text.getLeaves())
					.containsExactly(
						leaf("Lorem ipsum dolor sit amet, consectetur adipiscing elit. ")
					);

				final var icon = assertInstanceOf(Inline.Icon.class, content.getChildren().get(1));
				assertEquals("trash-plus", icon.getIcon());

				final var suffix = assertInstanceOf(Text.class, content.getChildren().get(2));
				assertThat(suffix.getLeaves())
					.containsExactly(
						leaf("")
					);
			}

			@Order(150)
			@DisplayName("Image")
			@Test
			void testImage() {
				final var title = assertInstanceOf(Block.Heading1.class, nodes.get(28));
				assertContainsSingleTextContent("Inline Image", title);

				final var content = assertInstanceOf(Block.Paragraph.class, nodes.get(29));

				final var text = assertInstanceOf(Text.class, content.getChildren().get(0));
				assertThat(text.getLeaves())
					.containsExactly(
						leaf("Lorem ipsum dolor sit amet, consectetur adipiscing elit. ")
					);

				final var image = assertInstanceOf(Inline.Image.class, content.getChildren().get(1));
				assertEquals("An alt text.", image.getAlt());
				assertInstanceOf(Reference.Url.class, image.getSource());
				assertNotNull(image.getSize());

				final var suffix = assertInstanceOf(Text.class, content.getChildren().get(2));
				assertThat(suffix.getLeaves())
					.containsExactly(
						leaf("")
					);
			}

			private Leaf leaf(String text, Mark... marks) {
				return new Leaf()
					.setText(text)
					.setMarks(Arrays.asList(marks));
			}

			private void doTest(int titleIndex, String titleContent, int contentIndex, String prefixContent, String textContent, Mark mark, String suffixContent) {
				final var title = assertInstanceOf(Block.Heading1.class, nodes.get(titleIndex));
				assertContainsSingleTextContent(titleContent, title);

				final var content = assertInstanceOf(Block.Paragraph.class, nodes.get(contentIndex));
				final var text = assertInstanceOf(Text.class, content.getChildren().get(0));
				assertThat(text.getLeaves())
					.containsExactly(
						leaf(prefixContent),
						leaf(textContent, mark),
						leaf(suffixContent)
					);
			}

		}

	}

	private List<Node> getNodes(String page) {
		var revisionPage = client.getSpaceContent(spaceId, page)
			.orElseThrow(() -> new IllegalStateException("%s not found in space %s".formatted(page, spaceId)));

		return revisionPage.getDocument().getNodes();
	}

	private static void assertContainsSingleParagraphWithTextContent(String expected, Block block) {
		final var children = block.getChildren();
		assertThat(children).hasSize(1);

		final var paragraph = assertInstanceOf(Paragraph.class, children.get(0));

		assertContainsSingleTextContent(expected, paragraph);
	}

	private static void assertIsSingleParagraphWithTextContent(String expected, List<Node> nodes) {
		assertThat(nodes).hasSize(1);

		final var paragraph = assertInstanceOf(Paragraph.class, nodes.get(0));

		assertContainsSingleTextContent(expected, paragraph);
	}

	private static void assertContainsSingleTextContent(String expected, Block block) {
		assertContainsSingleTextContent(expected, block.getChildren());
	}

	private static void assertContainsSingleTextContent(String expected, Inline inline) {
		assertContainsSingleTextContent(expected, inline.getChildren());
	}

	private static void assertContainsSingleTextContent(String expected, List<Node> children) {
		assertThat(children).hasSize(1);

		final var text = assertInstanceOf(Text.class, children.get(0));

		assertTextContent(expected, text);
	}

	private static void assertTextContent(String expected, final Text text) {
		final var leaves = text.getLeaves();
		assertThat(leaves).hasSize(1);

		final var leaf = leaves.get(0);
		assertEquals(expected, leaf.getText());
	}

}