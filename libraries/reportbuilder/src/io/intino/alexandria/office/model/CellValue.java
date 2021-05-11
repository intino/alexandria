package io.intino.alexandria.office.model;

public class CellValue extends Definition {
	private String data;
	private String color;
	private Style style;

	public CellValue(String data) {
		this.data = data;
	}

	public enum Style { Normal, Bold }

	public static CellValue from(String definition) {
		if (!definition.contains("[")) return new CellValue(definition);
		CellValue result = new CellValue(definition.substring(0, definition.indexOf("[")));
		propertiesToMap(definition.substring(definition.indexOf("[")+1, definition.length() - 1)).forEach(result::add);
		return result;
	}

	public Object data() {
		return data;
	}

	public String color() {
		return color;
	}

	public Style style() {
		return style;
	}

	public void add(String key, String value) {
		if (key.equalsIgnoreCase("color")) color = value;
		else if (key.equalsIgnoreCase("style")) style = Style.valueOf(firstUpperCase(value));
	}

}
