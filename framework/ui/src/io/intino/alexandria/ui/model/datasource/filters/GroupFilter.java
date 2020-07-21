package io.intino.alexandria.ui.model.datasource.filters;

import io.intino.alexandria.ui.model.datasource.Filter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupFilter extends Filter {
	private Set<String> groups;

	public GroupFilter(String grouping, List<String> groups) {
		super(grouping);
		this.groups = new HashSet<>(groups);
	}

	public Set<String> groups() {
		return groups;
	}

	public GroupFilter groups(List<String> groups) {
		this.groups = new HashSet<>(groups);
		return this;
	}
}
