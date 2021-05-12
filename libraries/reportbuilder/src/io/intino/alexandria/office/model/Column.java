package io.intino.alexandria.office.model;

import java.util.Locale;

import static java.lang.Double.parseDouble;

public class Column extends Definition {
	private final String label;
	private Type type = Type.Amount;
	private String color = DefaultColor;
	private String border;
	private int divideBy = 1;
	private int decimalsCount = 0;
	private Locale locale = Locale.ENGLISH;
	private boolean optional = false;

	public Column(String label) {
		this.label = label;
	}

	public static final String DefaultColor = "#FFFFFF";
	public enum Type { Text, Money, Ratio, Percentage, Amount, Dimension }
	public enum Alignment { Left, Center, Right }

	public static Column from(String definition) {
		if (!definition.contains("[")) return new Column(definition);
		Column result = new Column(definition.substring(0, definition.indexOf("[")));
		propertiesToMap(definition.substring(definition.indexOf("[")+1, definition.length() - 1)).forEach(result::add);
		return result;
	}

	public String label() {
		return label;
	}

	public Type type() {
		return type;
	}

	public String color() {
		return color;
	}

	public String border() {
		return border;
	}

	public boolean optional() {
		return optional;
	}

	public Alignment alignment() {
		if (type() == Type.Text) return Alignment.Left;
		return Alignment.Right;
	}

	public String valueOf(Object data) {
		if (type == Type.Money) return formattedNumber(amountValue(parseDouble((String)data), divideBy), decimalsCount, locale) + " $";
		else if (type == Type.Ratio || type == Type.Amount) return formattedNumber(amountValue(parseDouble((String)data), divideBy), decimalsCount, locale);
		else if (type == Type.Percentage) return data + " %";
		return String.valueOf(data);
	}

	public void add(String key, String value) {
		if (key.equalsIgnoreCase("type")) type = Type.valueOf(firstUpperCase(value));
		else if (key.equalsIgnoreCase("color")) color = value;
		else if (key.equalsIgnoreCase("border")) border = value;
		else if (key.equalsIgnoreCase("optional")) optional = Boolean.parseBoolean(value);
		else if (key.equalsIgnoreCase("divideBy")) divideBy = Integer.parseInt(value);
		else if (key.equalsIgnoreCase("decimals")) decimalsCount = Integer.parseInt(value);
		else if (key.equalsIgnoreCase("locale")) locale = Locale.forLanguageTag(value);
	}

}
