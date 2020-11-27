package io.intino.alexandria.ui.model.dynamictable;

public class Cell {
	private String name;
	private Double value;

	public Cell(String name, double value) {
		this.name = name;
		this.value = value;
	}

	public String name() {
		return name;
	}

	public Cell name(String name) {
		this.name = name;
		return this;
	}

	public Double value() {
		return value;
	}

	public Cell value(Double value) {
		this.value = value;
		return this;
	}
}
