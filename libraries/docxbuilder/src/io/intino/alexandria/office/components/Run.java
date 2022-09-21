package io.intino.alexandria.office.components;

/**
 * <p>
 *     A Run object represents a section of a paragraph with properties.
 * </p>
 *
 * */
public abstract class Run {

	protected StyleGroup styles = null; // Copy the template's field styles

	public StyleGroup styles() {
		return styles;
	}

	public Run styles(StyleGroup styles) {
		this.styles = styles;
		return this;
	}

	public Run styles(Style... styles) {
		if(this.styles == null) this.styles = new StyleGroup();
		for(Style s : styles) this.styles.add(s);
		return this;
	}

	public String xml() {
		StringBuilder sb = new StringBuilder();
		sb.append("<w:r>");

		if(styles != null) sb.append(styles.xml());

		sb.append(getValueXML());

		sb.append("</w:r>");
		return sb.toString();
	}

	protected abstract String getValueXML();
}
