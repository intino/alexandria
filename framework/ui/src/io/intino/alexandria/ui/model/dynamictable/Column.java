package io.intino.alexandria.ui.model.dynamictable;

public class Column {
	private String name;
	private Double value;

	public Column(String name, double value) {
		this.name = name;
		this.value = value;
	}

	public String name() {
		return name;
	}

	public Column name(String name) {
		this.name = name;
		return this;
	}

	public Double value() {
		return value;
	}

	public Column value(Double value) {
		this.value = value;
		return this;
	}
}
