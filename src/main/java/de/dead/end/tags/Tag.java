package de.dead.end.tags;

import org.apache.commons.text.StringEscapeUtils;

public class Tag {

	private static final String OPENING_TAG_START = "<";
	private static final String CLOSING_TAG_START = "</";
	private static final String TAG_END = ">";
	private static final String EMPTY_TAG_END = "/>";

	private static final String FMT_TAG_ALREADY_CLOSED = "Tag: '%s' is already closed!";

	private static final Tag ROOT_PARENT = null;

	/**
	 * The name of the tag.
	 */
	private final String name;

	/**
	 * The parent of the tag or null if no is present.
	 */
	private final Tag parent;

	/**
	 * The current child of the tag or null if no child is present.
	 */
	private Tag child;

	/**
	 * The state of the tag.
	 */
	private TagState state;

	/**
	 * The indent level of the tag, which is constant.
	 */
	private final int indentLevel;

	/**
	 * The configuration of the tag.
	 */
	private final TagConfig tagConfig;

	/**
	 * The method is called from the {@link de.dead.end.tags.TagBuilder} to create a
	 * root tag. Because the TagBilder is the only one that may call this method, it
	 * is package private.
	 *
	 * @param name
	 *            The name of the root tag.
	 * @param tagConfig
	 *            The tag configuration.
	 */
	Tag(final String name, final TagConfig tagConfig) {
		this(name, ROOT_PARENT, 0, tagConfig);
	}

	/**
	 * The constructor of the Tag class is private. New instances are created by the
	 * {@link de.dead.end.tags.TagBuilder} or the {@link #createChild(String)}
	 * method.
	 *
	 * @param name
	 *            The name of the tag.
	 * @param parent
	 *            The parent of the tag or null for the root tag.
	 * @param indentLevel
	 *            The indent level.
	 * @param tagConfig
	 *            The tag configuration.
	 */
	private Tag(final String name, final Tag parent, final int indentLevel, final TagConfig tagConfig) {
		this.name = name;
		this.parent = parent;
		this.indentLevel = indentLevel;

		this.tagConfig = tagConfig;

		this.state = TagState.OPENED;

		indentStart(this.indentLevel);
		add(OPENING_TAG_START, name);
	}

	public Tag createChild(final String name) {
		// System.out.println("createChild: " + name);

		switch (this.state) {
		case OPENED: {

			add(TAG_END);
			indentEnd();

			this.state = TagState.CONTENT;
			break;
		}
		case CONTENT: {
			closeChild();
			break;
		}
		default: {
			throw new IllegalArgumentException(String.format(FMT_TAG_ALREADY_CLOSED, this.name));
		}
		}

		return this.child = new Tag(name, this, this.indentLevel + 1, this.tagConfig);
	}

	/**
	 * The method adds an attribute to the tag with the default escape value.
	 *
	 * @param key
	 *            The key of the attribute.
	 * @param value
	 *            The value of the attribute, which may be escaped if configured.
	 * @return This tag.
	 */
	public Tag attr(final String key, final String value) {
		return attr(key, value, this.tagConfig.defaultDoEscape);
	}

	/**
	 * The method adds an attribute to the tag.
	 *
	 * @param key
	 *            The key of the attribute.
	 * @param value
	 *            The value of the attribute, which may be escaped.
	 * @param doEscape
	 *            The escape flag.
	 * @return This tag.
	 */
	public Tag attr(final String key, final String value, final boolean doEscape) {
		if (this.state != TagState.OPENED) {
			throw new IllegalArgumentException(String.format(FMT_TAG_ALREADY_CLOSED, this.name));
		}

		add(" ", key, "=\"", doEscape ? StringEscapeUtils.escapeXml11(value) : value, "\"");
		return this;
	}

	/**
	 * The method adds content to the tag, which may be escaped if configured.
	 *
	 * @param data
	 *            The content for the tag.
	 * @return This tag.
	 */
	public Tag content(final String data) {
		return content(data, this.tagConfig.defaultDoEscape);
	}

	/**
	 * The method adds content to the tag, which may be escaped.
	 *
	 * @param data
	 *            The content for the tag.
	 * @param doEscape
	 *            The content for the tag.
	 * @return This tag.
	 */
	public Tag content(final String data, final boolean doEscape) {

		switch (this.state) {
		case OPENED: {

			//
			// The tag is open (example: '<tag key="value"' ). Add ">" and a line separator
			//
			add(TAG_END);
			indentEnd();

			this.state = TagState.CONTENT;
		}
		case CONTENT: {
			closeChild();

			indentStart(this.indentLevel + 1);
			add(doEscape ? StringEscapeUtils.escapeXml11(data) : data);
			indentEnd();

			return this;
		}
		default: {
			throw new IllegalArgumentException(String.format(FMT_TAG_ALREADY_CLOSED, this.name));
		}
		}
	}

	/**
	 * The method closes the current tag.
	 */
	private void close() {
		// System.out.println("close(): " + this.name);

		switch (this.state) {
		case OPENED: {

			//
			// The tag is open (example: '<tag key="value"' ). Add "/>" and a line separator
			//
			add(EMPTY_TAG_END);
			indentEnd();

			break;
		}
		case CONTENT: {

			//
			// If the tag is in the content state, the end tag is missing. If configured the
			// end tag needs an indentation and a line separator.
			//
			indentStart(this.indentLevel);
			add(CLOSING_TAG_START, this.name, TAG_END);
			indentEnd();

			break;
		}
		default: {
			throw new IllegalArgumentException(String.format(FMT_TAG_ALREADY_CLOSED, this.name));
		}
		}
		this.state = TagState.CLOSED;
	}

	/**
	 * The method closes the child, if present, which closes its child if present
	 * and so on.
	 */
	private void closeChild() {
		// System.out.println("closeChild(): " + this.name);

		if (this.child != null) {
			this.child.closeChild();
			this.child.close();
			this.child = null;
		}
	}

	/**
	 * The method finishes the build process.
	 *
	 * @return The string with the XML or XHTML content.
	 */
	public String finish() {
		// System.out.println("finish(): " + this.name);

		//
		// Delegate the method call to the root tag.
		//
		if (this.parent != null) {
			return this.parent.finish();
		}

		//
		// The root tag closes the children and returns the result.
		//
		this.closeChild();
		this.close();
		return this.tagConfig.builder.toString();
	}

	/**
	 * The method returns the current state of the build process. In most cases this
	 * is not a valid XML or XHTML string.
	 */
	@Override
	public String toString() {
		return this.tagConfig.builder.toString();
	}

	/**
	 * The method adds the arguments to the buffer.
	 *
	 * @param args
	 *            The arguments for the buffer.
	 */
	private void add(final String... args) {

		for (final String arg : args) {
			this.tagConfig.builder.append(arg);
		}

		// System.out.println(Arrays.toString(args));
		// System.out.println(this.tagConfig.builder.toString());
	}

	/**
	 * The method adds spaces to the buffer for the indentation.
	 *
	 * @param level
	 *            The indent level
	 */
	private void indentStart(final int level) {
		if (this.tagConfig.doIndent) {

			final StringBuilder stringBuilder = this.tagConfig.builder;
			final int end = level * this.tagConfig.indentSize;

			for (int i = 0; i < end; i++) {
				stringBuilder.append(" ");
			}
		}
	}

	/**
	 * The method adds the line separator to the buffer, if indentation is
	 * configured.
	 */
	private void indentEnd() {
		if (this.tagConfig.doIndent) {
			this.tagConfig.builder.append(this.tagConfig.lineSeparator);
		}
	}
}
