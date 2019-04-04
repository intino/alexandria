package io.intino.alexandria.ui.model.datasource;

import java.util.Comparator;

public abstract class Sorting<O> {
	private String name;

	public String name() {
		return name;
	}

	public Sorting name(String name) {
		this.name = name;
		return this;
	}

	public abstract Comparator<O> comparator();
}
