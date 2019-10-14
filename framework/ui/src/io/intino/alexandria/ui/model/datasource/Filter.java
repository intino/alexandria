package io.intino.alexandria.ui.model.datasource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Filter {
	private final String grouping;
	private Set<String> groups;

	public Filter(String grouping, List<String> groups) {
		this.grouping = grouping;
		this.groups = new HashSet<>(groups);
	}

	public String grouping() {
		return grouping;
	}

	public Set<String> groups() {
		return groups;
	}

	public Filter groups(List<String> groups) {
		this.groups = new HashSet<>(groups);
		return this;
	}
}
