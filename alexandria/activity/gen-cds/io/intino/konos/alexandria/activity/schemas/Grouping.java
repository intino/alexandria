package io.intino.konos.alexandria.activity.schemas;

public class Grouping implements java.io.Serializable {

	private String name = "";
	private String type = "";
	private String label = "";
	private Integer countItems = 0;
	private String histogram = "";
	private java.util.List<Group> groupList = new java.util.ArrayList<>();

	public String name() {
		return this.name;
	}

	public String type() {
		return this.type;
	}

	public String label() {
		return this.label;
	}

	public Integer countItems() {
		return this.countItems;
	}

	public String histogram() {
		return this.histogram;
	}

	public java.util.List<Group> groupList() {
		return this.groupList;
	}

	public Grouping name(String name) {
		this.name = name;
		return this;
	}

	public Grouping type(String type) {
		this.type = type;
		return this;
	}

	public Grouping label(String label) {
		this.label = label;
		return this;
	}

	public Grouping countItems(Integer countItems) {
		this.countItems = countItems;
		return this;
	}

	public Grouping histogram(String histogram) {
		this.histogram = histogram;
		return this;
	}

	public Grouping groupList(java.util.List<Group> groupList) {
		this.groupList = groupList;
		return this;
	}
}