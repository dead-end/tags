package de.dead.end.tags;

/**
 * This class contains the configurations for the {@link de.dead.end.tags.Tag}
 * instances. The class is immutable and shared by all Tag instances. The class
 * is created by the {@link de.dead.end.tags.TagBuilder}, so there is no need
 * for setters / getters.
 *
 * @author Dead End
 *
 */
class TagConfig {
	static final int INDENT_DISABLED = 0;

	final boolean defaultDoEscape;

	final int indentSize;

	final String lineSeparator;

	final boolean doIndent;

	final StringBuilder builder;

	/**
	 * The constructor for the immutable class.
	 *
	 * @param declaration
	 *            XML or XHTML declaration.
	 * @param indentSize
	 *            Number of chars for a indentation.
	 * @param defaultDoEscape
	 *            The default value for escape.
	 * @param lineSeparator
	 *            The configured line separator.
	 */
	TagConfig(final String declaration, final int indentSize, final boolean defaultDoEscape,
			final String lineSeparator) {
		super();
		this.defaultDoEscape = defaultDoEscape;
		this.indentSize = indentSize;
		this.builder = new StringBuilder();
		this.lineSeparator = lineSeparator;

		this.doIndent = this.indentSize != INDENT_DISABLED;

		if (declaration != null) {
			this.builder.append(declaration);
			this.builder.append(this.lineSeparator);
		}
	}
}
