package de.dead.end.tags;

/**
 * The class is used to create the configurations for the
 * {@link de.dead.end.tags.Tag} instances.The configurations are stored in the
 * {@link de.dead.end.tags.TagConfig} class, which is immutable and shared by
 * all Tag instances.
 *
 * @author Dead End
 *
 */
public class TagBuilder {
	private static final boolean DO_ESCAPE_DEFAULT = true;

	/**
	 * The XML or XHTML declaration. If null the declaration will be ignored.
	 */
	private String declaration = null;

	/**
	 * The default escape flag.
	 */
	private boolean doEscape = DO_ESCAPE_DEFAULT;

	/**
	 * The number of chars for an indentation. If 0 the indentation is disabled.
	 */
	private int indentSize = TagConfig.INDENT_DISABLED;

	/**
	 * The line separator for the indentation. The default is the system line
	 * separator.
	 */
	private String lineSeparator = System.lineSeparator();

	/**
	 * The method creates a root tag with the given name and an instance of
	 * {@link de.dead.end.tags.TagConfig} with the configurations.
	 *
	 * @param name
	 *            The name of the root tag.
	 * @return The root tag.
	 */
	public Tag createRootTag(final String name) {
		return new Tag(name, new TagConfig(this.declaration, this.indentSize, this.doEscape, this.lineSeparator));
	}

	public boolean hasDeclaration(final String declaration) {
		return (this.declaration != null);
	}

	public String getDeclaration() {
		return this.declaration;
	}

	public TagBuilder setDeclaration(final String declaration) {
		this.declaration = declaration;
		return this;
	}

	public boolean isDoEscape() {
		return this.doEscape;
	}

	public void setDoEscape(final boolean doEscape) {
		this.doEscape = doEscape;
	}

	public int getIndentSize() {
		return this.indentSize;
	}

	public TagBuilder setIndentSize(final int indentSize) {
		if (indentSize < 0) {
			this.indentSize = TagConfig.INDENT_DISABLED;
		}
		this.indentSize = indentSize;
		return this;
	}

	public String getLineSeparator() {
		return this.lineSeparator;
	}

	public TagBuilder setLineSeparator(final String lineSeparator) {
		this.lineSeparator = lineSeparator;
		return this;
	}
}
