package dev.caceresenzo.gitbook.model.document;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import dev.caceresenzo.gitbook.client.GitBookClient;
import dev.caceresenzo.gitbook.model.document.Block.Paragraph;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NodeTest {

	public static final String ACCESS_TOKEN_ENV_VAR = "GITBOOK_ACCESS_TOKEN";
	public static final String SPACE_ID_ENV_VAR = "GITBOOK_COMPONENTS_SPACE_ID";
	public static final String LOREM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

	static GitBookClient client;
	static String spaceId;

	@BeforeAll
	static void setUp() {
		final var accessToken = getEnvironmentVariable(ACCESS_TOKEN_ENV_VAR);
		spaceId = getEnvironmentVariable(SPACE_ID_ENV_VAR);

		client = GitBookClient.builder()
			.accessToken(accessToken)
			.build();
	}

	@Order(1000)
	@DisplayName("Text")
	@Test
	void testText() {
		final var nodes = getNodes("/simple/text");
		assertThat(nodes).hasSize(4);

		{
			final var paragraph = assertInstanceOf(Block.Paragraph.class, nodes.get(0));

			assertSingleTextContent(LOREM, paragraph);
		}

		{
			final var heading = assertInstanceOf(Block.Heading1.class, nodes.get(1));

			assertEquals("heading-1", heading.getId());
			assertSingleTextContent("Heading 1", heading);
		}

		{
			final var heading = assertInstanceOf(Block.Heading2.class, nodes.get(2));

			assertEquals("heading-2", heading.getId());
			assertSingleTextContent("Heading 2", heading);
		}

		{
			final var heading = assertInstanceOf(Block.Heading3.class, nodes.get(3));

			assertEquals("heading-3", heading.getId());
			assertSingleTextContent("Heading 3", heading);
		}
	}

	@Order(1010)
	@DisplayName("List")
	@Test
	void testList() {
		final var nodes = getNodes("/simple/list");
		assertThat(nodes).hasSize(6);

		{
			final var heading = assertInstanceOf(Block.Heading1.class, nodes.get(0));
			assertSingleTextContent("Unordered", heading);

			final var list = assertInstanceOf(Block.UnorderedList.class, nodes.get(1));

			final var listItems = list.getChildren();
			assertThat(listItems).hasSize(2);

			final var firstItem = assertInstanceOf(Block.ListItem.class, listItems.get(0));
			assertNull(firstItem.getChecked());
			assertSingleParagraphWithTextContent("Item #1", firstItem);

			final var secondItem = assertInstanceOf(Block.ListItem.class, listItems.get(1));
			assertNull(secondItem.getChecked());
			assertSingleParagraphWithTextContent("Item #2", secondItem);
		}

		{
			final var heading = assertInstanceOf(Block.Heading1.class, nodes.get(2));
			assertSingleTextContent("Ordered", heading);

			final var list = assertInstanceOf(Block.OrderedList.class, nodes.get(3));

			final var listItems = list.getChildren();
			assertThat(listItems).hasSize(2);

			final var firstItem = assertInstanceOf(Block.ListItem.class, listItems.get(0));
			assertNull(firstItem.getChecked());
			assertSingleParagraphWithTextContent("Item #1", firstItem);

			final var secondItem = assertInstanceOf(Block.ListItem.class, listItems.get(1));
			assertNull(secondItem.getChecked());
			assertSingleParagraphWithTextContent("Item #2", secondItem);
		}

		{
			final var heading = assertInstanceOf(Block.Heading1.class, nodes.get(4));
			assertSingleTextContent("Task", heading);

			final var list = assertInstanceOf(Block.TaskList.class, nodes.get(5));

			final var listItems = list.getChildren();
			assertThat(listItems).hasSize(3);

			final var firstItem = assertInstanceOf(Block.ListItem.class, listItems.get(0));
			assertEquals(Boolean.FALSE, firstItem.getChecked());
			assertSingleParagraphWithTextContent("Item #1", firstItem);

			final var secondItem = assertInstanceOf(Block.ListItem.class, listItems.get(1));
			assertEquals(Boolean.FALSE, secondItem.getChecked());
			assertSingleParagraphWithTextContent("Item #2", secondItem);

			final var thirdItem = assertInstanceOf(Block.ListItem.class, listItems.get(2));
			assertEquals(Boolean.TRUE, thirdItem.getChecked());
			assertSingleParagraphWithTextContent("Checked", thirdItem);
		}
	}

	@Order(1020)
	@DisplayName("Hint")
	@Test
	void testHint() {
		final var nodes = getNodes("/simple/hint");
		assertThat(nodes).hasSize(4);

		{
			final var hint = assertInstanceOf(Block.Hint.class, nodes.get(0));
			assertEquals(Block.Hint.Style.INFO, hint.getStyle());

			assertSingleParagraphWithTextContent(LOREM, hint);
		}

		{
			final var hint = assertInstanceOf(Block.Hint.class, nodes.get(1));
			assertEquals(Block.Hint.Style.WARNING, hint.getStyle());

			assertSingleParagraphWithTextContent(LOREM, hint);
		}

		{
			final var hint = assertInstanceOf(Block.Hint.class, nodes.get(2));
			assertEquals(Block.Hint.Style.DANGER, hint.getStyle());

			assertSingleParagraphWithTextContent(LOREM, hint);
		}

		{
			final var hint = assertInstanceOf(Block.Hint.class, nodes.get(3));
			assertEquals(Block.Hint.Style.SUCCESS, hint.getStyle());

			assertSingleParagraphWithTextContent(LOREM, hint);
		}
	}

	@Order(1030)
	@DisplayName("Quote")
	@Test
	void testQuote() {
		final var nodes = getNodes("/simple/quote");
		assertThat(nodes).hasSize(1);

		{
			final var hint = assertInstanceOf(Block.Quote.class, nodes.get(0));

			assertSingleParagraphWithTextContent(LOREM, hint);
		}
	}

	@Order(1040)
	@DisplayName("Divider")
	@Test
	void testDivider() {
		final var nodes = getNodes("/simple/divider");
		assertThat(nodes).hasSize(3);

		assertInstanceOf(Block.Paragraph.class, nodes.get(0));
		assertInstanceOf(Block.Divider.class, nodes.get(1));
		assertInstanceOf(Block.Paragraph.class, nodes.get(2));
	}

	@Order(1050)
	@DisplayName("Code Block")
	@Test
	void testCodeBlock() {
		final var nodes = getNodes("/simple/code-block");
		assertThat(nodes).hasSize(4);

		{
			final var heading = assertInstanceOf(Block.Heading1.class, nodes.get(0));
			assertSingleTextContent("Regular", heading);

			final var code = assertInstanceOf(Block.Code.class, nodes.get(1));
			assertEquals("python", code.getSyntax());

			final var lines = code.getChildren();
			assertThat(lines).hasSize(1);

			final var line = assertInstanceOf(Block.CodeLine.class, lines.get(0));
			assertSingleTextContent("print(42)", line);
		}

		{
			final var heading = assertInstanceOf(Block.Heading1.class, nodes.get(2));
			assertSingleTextContent("With options", heading);

			final var code = assertInstanceOf(Block.Code.class, nodes.get(3));
			assertEquals("python", code.getSyntax());
			assertTrue(code.isLineNumbers());
			assertEquals("hello.py", code.getTitle());
			assertTrue(code.isExpandable());

			final var lines = code.getChildren();
			assertThat(lines).hasSize(1);

			final var line = assertInstanceOf(Block.CodeLine.class, lines.get(0));
			assertSingleTextContent("print(42)", line);
		}
	}

	private void assertSingleParagraphWithTextContent(String expected, Block block) {
		final var children = block.getChildren();
		assertThat(children).hasSize(1);

		final var paragraph = assertInstanceOf(Paragraph.class, children.get(0));

		assertSingleTextContent(expected, paragraph);
	}

	private void assertSingleTextContent(String expected, Block block) {
		final var children = block.getChildren();
		assertThat(children).hasSize(1);

		final var text = assertInstanceOf(Text.class, children.get(0));

		final var leaves = text.getLeaves();
		assertThat(leaves).hasSize(1);

		final var leaf = leaves.get(0);
		assertEquals(expected, leaf.getText());
	}

	private List<Node> getNodes(String page) {
		var revisionPage = client.getSpaceContent(spaceId, page)
			.orElseThrow(() -> new IllegalStateException("%s not found in space %s".formatted(page, spaceId)));

		return revisionPage.getDocument().getNodes();
	}

	private static String getEnvironmentVariable(String name) {
		String value = System.getenv(name);
		if (value == null || value.isBlank()) {
			throw new IllegalStateException("%s is not set".formatted(name));
		}

		return value;
	}

}