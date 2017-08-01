package de.dead.end.tags;

import de.dead.end.tags.Tag;
import de.dead.end.tags.TagBuilder;

/**
 * An example class which shows how to create a html page with input data and a
 * template class.
 *
 * @author Dead End
 *
 */
public class TableTemplateExample {

	/**
	 * The method creates the head block with the css definitions.
	 *
	 * @param parent
	 *            The html tag.
	 */
	private void createHead(final Tag parent) {
		final Tag style = parent.createChild("head").createChild("style");
		style.content("table {border-collapse: collapse;}");
		style.content("th, td {text-align: left; padding: 8px;}");
		style.content("tr:nth-child(even) {background-color: #f2f2f2}");
	}

	/**
	 * The method creates the html body with the table.
	 *
	 * @param parent
	 *            The html tag.
	 * @param data
	 *            The array with the data for the table.
	 */
	private void createBody(final Tag parent, final String[][] data) {
		final Tag body = parent.createChild("body");
		body.createChild("h1").content("Persons");
		body.createChild("p").content("A list of persons of interest.");
		createTable(body, data);
	}

	/**
	 * The method creates the table with the given data. The data is not trusted, so
	 * it will be escaped.
	 *
	 * @param parent
	 *            The body tag.
	 * @param data
	 *            The data for the table.
	 */
	private void createTable(final Tag parent, final String[][] data) {

		final Tag table = parent.createChild("table").attr("width", "100%");

		Tag tr = table.createChild("tr");
		tr.createChild("th").content("First name");
		tr.createChild("th").content("Last name");
		tr.createChild("th").content("Age");

		for (int i = 0; i < data.length; i++) {
			tr = table.createChild("tr");
			for (int j = 0; j < data[i].length; j++) {
				tr.createChild("td").content(data[i][j], true);
			}
		}
	}

	/**
	 * The method creates the html page with the table. This includes creating a
	 * TagBuilder for the configurations.
	 *
	 * @param data
	 *            The table data.
	 * @return A String with the resulting html.
	 */
	private String getHtml(final String[][] data) {

		final TagBuilder tagBuilder = new TagBuilder();
		tagBuilder.setDeclaration("<!DOCTYPE html>");
		tagBuilder.setIndentSize(2);
		tagBuilder.setDoEscape(false);

		final Tag html = tagBuilder.createRootTag("html");
		createHead(html);
		createBody(html, data);
		return html.finish();
	}

	public static void main(final String[] args) {

		final String data[][] = { { "John", "Smith", "39" }, { "Jim", "Bo", "14" }, { "Mary", "Me", "25" } };

		final TableTemplateExample tableTemplateExample = new TableTemplateExample();
		final String html = tableTemplateExample.getHtml(data);
		System.out.println(html);
	}

}
