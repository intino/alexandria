package io.intino.alexandria.ui.model.datasource;

import java.util.List;

public class Filter {
	private final String grouping;
	private final List<String> groups;

	public Filter(String grouping, List<String> groups) {
		this.grouping = grouping;
		this.groups = groups;
	}

	public String grouping() {
		return grouping;
	}

	public List<String> groups() {
		return groups;
	}
}
