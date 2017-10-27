package io.intino.konos.server.activity.displays.schemas;

public class Catalog implements java.io.Serializable {

	private String name = "";
	private String label = "";
	private Boolean embedded = true;
	private Boolean hideGroupings = true;
	private java.util.List<Grouping> groupingList = new java.util.ArrayList<>();
	private java.util.List<Sorting> sortingList = new java.util.ArrayList<>();

	public String name() {
		return this.name;
	}

	public String label() {
		return this.label;
	}

	public Boolean embedded() {
		return this.embedded;
	}

	public Boolean hideGroupings() {
		return this.hideGroupings;
	}

	public java.util.List<Grouping> groupingList() {
		return this.groupingList;
	}

	public java.util.List<Sorting> sortingList() {
		return this.sortingList;
	}

	public Catalog name(String name) {
		this.name = name;
		return this;
	}

	public Catalog label(String label) {
		this.label = label;
		return this;
	}

	public Catalog embedded(Boolean embedded) {
		this.embedded = embedded;
		return this;
	}

	public Catalog hideGroupings(Boolean hideGroupings) {
		this.hideGroupings = hideGroupings;
		return this;
	}

	public Catalog groupingList(java.util.List<Grouping> groupingList) {
		this.groupingList = groupingList;
		return this;
	}

	public Catalog sortingList(java.util.List<Sorting> sortingList) {
		this.sortingList = sortingList;
		return this;
	}
}