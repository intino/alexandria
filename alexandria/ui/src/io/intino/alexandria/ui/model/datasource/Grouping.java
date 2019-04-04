package io.intino.alexandria.ui.model.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class Grouping<O> {
	private String name;
	private List<Group> groups = new ArrayList<>();

	public String name() {
		return name;
	}

	public Grouping name(String name) {
		this.name = name;
		return this;
	}

	public Grouping add(Group group) {
		this.groups.add(group);
		return this;
	}

	public abstract List<Group> groups(List<O> items);
	public abstract Predicate<O> filter(List<O> items);
}
