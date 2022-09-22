package io.intino.alexandria.office.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class StyleGroup {

	private final List<Style> styles = new ArrayList<>();

	public StyleGroup() {
	}

	public StyleGroup(Style... styles) {
		this.styles.addAll(Arrays.asList(styles));
	}

	public StyleGroup add(Style style) {
		this.styles.add(requireNonNull(style));
		return this;
	}

	public List<Style> styles() {
		return Collections.unmodifiableList(styles);
	}

	public String xml() {
		StringBuilder sb = new StringBuilder();
		sb.append("<w:rPr>");
		styles.forEach(style -> sb.append(style.xml()));
		sb.append("<w:noProof/>"); // TODO: check
		sb.append("</w:rPr>");
		return sb.toString();
	}

	@Override
	public String toString() {
		return xml();
	}
}
