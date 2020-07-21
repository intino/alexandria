package io.intino.alexandria.ui.model.datasource;

public abstract class Filter {
	private final String grouping;

	public Filter(String grouping) {
		this.grouping = grouping;
	}

	public String grouping() {
		return grouping;
	}
}
