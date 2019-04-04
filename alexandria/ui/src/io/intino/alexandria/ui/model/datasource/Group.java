package io.intino.alexandria.ui.model.datasource;

public class Group {
	private String name;
	private int count;

	public String name() {
		return name;
	}

	public Group name(String name) {
		this.name = name;
		return this;
	}

	public int count() {
		return count;
	}

	public Group count(int count) {
		this.count = count;
		return this;
	}
}
