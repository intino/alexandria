package io.intino.alexandria.ui.model.datasource;

public class Group {
	private String label;
	private int count;
	private String color;

	public String label() {
		return label;
	}

	public Group label(String label) {
		this.label = label;
		return this;
	}

	public int count() {
		return count;
	}

	public Group count(int count) {
		this.count = count;
		return this;
	}

	public String color() {
		return color;
	}

	public Group color(String color) {
		this.color = color;
		return this;
	}
}
