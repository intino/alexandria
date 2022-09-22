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

	private Alignment alignment = null; // Copy the template's field alignment
	private final List<Run> runs = new ArrayList<>();
	private String lineSeparator = "\n";

	public Paragraph() {
	}

	public Paragraph text(String text, Style... styles) {
		String[] lines = text.split(lineSeparator, -1);
		if(lines.length == 1) return addRun(new Text(text).styles(new StyleGroup(styles)));
		for(String line : lines) {
			addRun(new Text(line).styles(new StyleGroup(styles)));
			br();
		}
		return this;
	}

	public Paragraph text(Text text) {
		return addRun(text);
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
}
