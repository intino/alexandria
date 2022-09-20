package io.intino.alexandria.office.components;

/**
 * <p>
 *     Represents a docx paragraph. A paragraph contains:
 * </p>
 * <ul>
 *     <li>Alignment: Left, Center or Right</li>
 *     <li>Styles: list of styles, subclasses of the Style interface</li>
 *     <li>Text: if multiline, each line will have the same alignment and styles than the paragraph</li>
 * </ul>
 * <p>
 *     If the alignment is not set, it will copy the template's alignment of the merge field.
 * </p>
 * <p>
 *     If the styles are not set, it will copy the template's styles of the merge field.
 * </p>
 * <p>
 *		Reusing the same Paragraph object to replace different merge fields is NOT recommended.
 * <p>
 *
 * **/
public class Paragraph {

	private static final String BREAK_LINE = "<w:br/>";

	private StyleGroup styles = null; // Copy the template's field styles
	private Alignment alignment = null; // Copy the template's field alignment
	private String text = "";
	private String lineSeparator = "\n";

	public Paragraph() {
	}

	public Paragraph(String text) {
		this.text = text;
	}

	public StyleGroup styles() {
		return styles;
	}

	public Paragraph styles(StyleGroup styles) {
		this.styles = styles;
		return this;
	}

	public Paragraph styles(Style... styles) {
		if(this.styles == null) this.styles = new StyleGroup();
		for(Style s : styles) this.styles.add(s);
		return this;
	}

	/**
	 * Set no styles (default docx styles for paragraph will be applied)
	 * */
	public Paragraph withNoStyles() {
		styles = new StyleGroup();
		return this;
	}

	/**
	 * Set the styles to null to copy styles from template
	 * */
	public Paragraph withDefaultStyles() {
		styles = null;
		return this;
	}

	public String text() {
		return text;
	}

	public Paragraph text(String text) {
		this.text = text;
		return this;
	}

	public String lineSeparator() {
		return lineSeparator;
	}

	public Paragraph lineSeparator(String lineSeparator) {
		this.lineSeparator = lineSeparator;
		return this;
	}

	public Alignment alignment() {
		return alignment;
	}

	public Paragraph alignment(Alignment alignment) {
		this.alignment = alignment;
		return this;
	}

	public String xml() {
		StringBuilder sb = new StringBuilder();
		sb.append("<w:p>");

		// Properties block
		sb.append("<w:pPr>");
		if(alignment != null) sb.append(alignment.xml());
		if(styles != null) sb.append(styles.xml());
		sb.append("</w:pPr>");

		setText(sb, text.split(lineSeparator, -1));

		sb.append("</w:p>");
		return sb.toString();
	}

	private void setText(StringBuilder sb, String[] lines) {
		StyleGroup brStyles = new StyleGroup();
		for(String line : lines) {
			setRunBlock(sb, textBlock(line), this.styles);
			setRunBlock(sb, BREAK_LINE, brStyles);
		}
	}

	private void setRunBlock(StringBuilder sb, String node, StyleGroup styles) {
		// Run block
		sb.append("<w:r>");
		// Styles
		if(styles != null) sb.append(styles.xml());
		// Node
		sb.append(node);
		// Close run block
		sb.append("</w:r>");
	}

	private String textBlock(String text) {
		return "<w:t>" + text + "</w:t>";
	}

	@Override
	public String toString() {
		return xml();
	}
}
