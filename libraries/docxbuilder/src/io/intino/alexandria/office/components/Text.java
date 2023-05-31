package io.intino.alexandria.office.components;

import java.util.regex.Pattern;

public class Text extends Run {

	private String text = "";

	public Text() {
	}

	public Text(String text) {
		this.text(text);
	}

	public String text() {
		return text;
	}

	public Text text(String text) {
		this.text = normalizeXmlText(text);
		return this;
	}

	@Override
	public Text styles(StyleGroup styles) {
		return (Text) super.styles(styles);
	}

	@Override
	public Text styles(Style... styles) {
		return (Text) super.styles(styles);
	}

	@Override
	protected String getValueXML() {
		return "<w:t xml:space=\"preserve\">" + text + "</w:t>";
	}

	private static final Pattern AMP_Pattern = Pattern.compile("&(?!amp;)");
	public static String normalizeXmlText(String s) {
		return AMP_Pattern.matcher(s).replaceAll("&amp;");
	}
}
