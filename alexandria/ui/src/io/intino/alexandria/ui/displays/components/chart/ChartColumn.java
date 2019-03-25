package io.intino.alexandria.ui.displays.components.chart;

import java.util.ArrayList;
import java.util.List;

public class ChartColumn {
	public String name;
	public Type type = Type.Double;
	public List<Object> values = new ArrayList<>();

	public enum Type {
		Double, Integer, String
	}

	public String name() {
		return name;
	}

	public ChartColumn name(String name) {
		this.name = name;
		return this;
	}

	public Type type() {
		return type;
	}

	public ChartColumn type(Type type) {
		this.type = type;
		return this;
	}

	public List<Object> values() {
		return values;
	}

	public ChartColumn values(List<Object> values) {
		this.values = values;
		return this;
	}

	public ChartColumn add(Object value) {
		values.add(parse(value));
		return this;
	}

	private Object parse(Object value) {
		if (!(value instanceof String)) return value;
		if (type == Type.Double) return Double.parseDouble((String) value);
		else if (type == Type.Integer) return Integer.parseInt((String) value);
		return value;
	}
}
