package io.intino.konos.alexandria.activity.schemas;

public class GroupingSelection implements java.io.Serializable {

	private String name = "";
	private java.util.List<String> groups = new java.util.ArrayList<>();

	public String name() {
		return this.name;
	}

	public java.util.List<String> groups() {
		return this.groups;
	}

	public GroupingSelection name(String name) {
		this.name = name;
		return this;
	}

	public GroupingSelection groups(java.util.List<String> groups) {
		this.groups = groups;
		return this;
	}
}