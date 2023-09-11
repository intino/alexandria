package io.intino.alexandria.ui.model.datasource.grid;

import java.time.Instant;

public class GridValue {
	private Object value;
	private String color;

	public GridValue(Object value) {
		this(value, null);
	}

	public GridValue(Object value, String color) {
		this.value = value;
		this.color = color;
	}

	public String asText() {
		if (value == null) return null;
		if (value instanceof String) return (String) value;
		if (isNumber()) return String.valueOf(asNumber());
		if (isInstant()) return String.valueOf(asInstant());
		return (String) value;
	}

	public String color() {
		return color;
	}

	public boolean isNumber() {
		return value instanceof Double || value instanceof Integer;
	}

	public Double asNumber() {
		if (value == null) return null;
		if (value instanceof Double) return (Double)value;
		if (value instanceof Integer) return ((Integer)value).doubleValue();
		return Double.parseDouble((String) value);
	}

	public boolean isInstant() {
		return value instanceof Instant;
	}

	public Instant asInstant() {
		if (value == null) return null;
		if (value instanceof Instant) return (Instant)value;
		if (value instanceof Long) return Instant.ofEpochMilli((Long)value);
		return Instant.parse((String) value);
	}

	public GridValue value(Object value) {
		this.value = value;
		return this;
	}

}
