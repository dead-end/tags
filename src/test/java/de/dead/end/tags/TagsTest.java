package de.dead.end.tags;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TagsTest {

	@Test
	public void test() {
		final TagBuilder tagBuilder = new TagBuilder();

		Tag tag = null;

		tag = tagBuilder.createRootTag("root");
		assertEquals("<root/>", tag.finish());

		tag = tagBuilder.createRootTag("root").attr("key1", "value1");
		assertEquals("<root key1=\"value1\"/>", tag.finish());

		tag = tagBuilder.createRootTag("root").attr("key1", "value1").attr("key2", "value2");
		assertEquals("<root key1=\"value1\" key2=\"value2\"/>", tag.finish());

		tag = tagBuilder.createRootTag("root").createChild("child1");
		assertEquals("<root><child1/></root>", tag.finish());

		tag = tagBuilder.createRootTag("root").createChild("child1").createChild("child2");
		assertEquals("<root><child1><child2/></child1></root>", tag.finish());

		tag = tagBuilder.createRootTag("root");
		tag.createChild("child1").attr("key1", "value1");
		tag.createChild("child2").attr("key2", "value2");

		assertEquals("<root><child1 key1=\"value1\"/><child2 key2=\"value2\"/></root>", tag.finish());

		tag = tagBuilder.createRootTag("root");
		tag.createChild("child1").attr("key1", "value1").createChild("child2");
		tag.createChild("child3");

		assertEquals("<root><child1 key1=\"value1\"><child2/></child1><child3/></root>", tag.finish());

	}

	@Test
	public void testExceptions() {
		final TagBuilder tagBuilder = new TagBuilder();

		Tag tag = null;

		tag = tagBuilder.createRootTag("root").createChild("child1");
		tag.createChild("child2");
		try {
			tag.attr("key1", "value1");
			fail("Exception expected!");
		} catch (final IllegalArgumentException iae) {
		}

		tag = tagBuilder.createRootTag("root").createChild("child1");
		final Tag t1 = tag.createChild("child2");
		tag.createChild("child3");
		try {
			t1.createChild("child4");
			fail("Exception expected: " + tag.toString());
		} catch (final IllegalArgumentException iae) {
		}
	}

	@Test
	public void testAppend() {
		final TagBuilder tagBuilder = new TagBuilder();

		Tag tag = null;

		//
		// ensure everything will be closed before appending content
		//
		tag = tagBuilder.createRootTag("ROOT");
		tag.createChild("CH1").createChild("CH2").attr("key1", "value1").content("c1");
		assertEquals("<ROOT><CH1><CH2 key1=\"value1\">c1</CH2></CH1></ROOT>", tag.finish());

		//
		// ensure that content can be added multiple times
		//
		tag = tagBuilder.createRootTag("ROOT");
		tag.content("c0");
		tag.createChild("CH1").content("c1");
		tag.content("c2");
		tag.content("c3");
		tag.createChild("CH2");
		tag.content("c4");
		assertEquals("<ROOT>c0<CH1>c1</CH1>c2c3<CH2/>c4</ROOT>", tag.finish());
	}

	@Test
	public void testIndent() {
		final TagBuilder tagBuilder = new TagBuilder();
		tagBuilder.setIndentSize(2);

		Tag tag = null;

		//
		// ensure everything will be closed before appending content
		//
		tag = tagBuilder.createRootTag("ROOT");

		final Tag ch1 = tag.createChild("CH1");
		ch1.createChild("CH2").attr("key1", "value1").content("c1").content("c2");
		ch1.createChild("CH3");

		final StringBuilder builder = new StringBuilder();
		builder.append("<ROOT>").append(System.lineSeparator());
		builder.append("  <CH1>").append(System.lineSeparator());
		builder.append("    <CH2 key1=\"value1\">").append(System.lineSeparator());
		builder.append("      c1").append(System.lineSeparator());
		builder.append("      c2").append(System.lineSeparator());
		builder.append("    </CH2>").append(System.lineSeparator());
		builder.append("    <CH3/>").append(System.lineSeparator());
		builder.append("  </CH1>").append(System.lineSeparator());
		builder.append("</ROOT>").append(System.lineSeparator());

		assertEquals(builder.toString(), tag.finish());

	}
}
