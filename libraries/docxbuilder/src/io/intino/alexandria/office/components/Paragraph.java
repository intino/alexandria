package io.intino.alexandria.office.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * <p>
 *     Represents a docx paragraph. A paragraph contains:
 * </p>
 * <ul>
 *     <li>Alignment: Left, Center or Right</li>
 *     <li>Runs: if multiline, each line will have the same alignment and styles than the paragraph</li>
 * </ul>
 * <p>
 *     If the alignment is not set, it will copy the template's alignment of the merge field.
 * </p>
 * <p>
 *		Reusing the same Paragraph object to replace different merge fields is NOT recommended.
 * <p>
 *
 * **/
public class Paragraph {

	private static final String CDATA_BEGIN = "<![CDATA[";
	private static final String CDATA_END = "]]>";

	private Alignment alignment = null; // Copy the template's field alignment
	private final List<Run> runs = new ArrayList<>();
	private String lineSeparator = "\n";

	public Paragraph() {
	}

	public Paragraph text(String text, Style... styles) {
		boolean cdata = text.startsWith(CDATA_BEGIN) && text.endsWith(CDATA_END);
		String[] lines = text.split(lineSeparator, -1);
		if(lines.length == 1) return addRun(new Text(text).styles(new StyleGroup(styles)));
		for(String line : lines) {
			if(cdata) line = withCDATA(line);
			addRun(new Text(line).styles(new StyleGroup(styles)));
			br();
		}
		return this;
	}

	public Paragraph text(Text text) {
		return text(text.text(), text.styles() == null ? new Style[0] : text.styles().styles().toArray(new Style[0]));
	}

	public Paragraph br() {
		return addRun(new Br());
	}

	public Paragraph addRun(Run run) {
		this.runs.add(requireNonNull(run));
		return this;
	}

	public List<Run> runs() {
		return Collections.unmodifiableList(runs);
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
		sb.append("</w:pPr>");

		runs.forEach(r -> sb.append(r.xml()));

		sb.append("</w:p>");
		return sb.toString();
	}

	@Override
	public String toString() {
		return xml();
	}

	private String withCDATA(String value) {
		if(value.startsWith(CDATA_BEGIN)) return value + CDATA_END;
		if(value.endsWith(CDATA_END)) return CDATA_BEGIN + value;
		return CDATA_BEGIN + value + CDATA_END;
	}
}
