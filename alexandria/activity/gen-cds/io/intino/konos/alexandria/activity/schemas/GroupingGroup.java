package io.intino.konos.alexandria.activity.schemas;

public class GroupingGroup implements java.io.Serializable {

	private String grouping = "";
	private String name = "";

	public String grouping() {
		return this.grouping;
	}

	public String name() {
		return this.name;
	}

	public GroupingGroup grouping(String grouping) {
		this.grouping = grouping;
		return this;
	}

	public GroupingGroup name(String name) {
		this.name = name;
		return this;
	}
}