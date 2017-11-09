package io.intino.konos.alexandria.activity.schemas;

public class Operation implements java.io.Serializable {

	private String name = "";
	private String title = "";
	private String type = "";
	private String icon = "";
	private String when = "";
	private java.util.List<Property> propertyList = new java.util.ArrayList<>();

	public String name() {
		return this.name;
	}

	public String title() {
		return this.title;
	}

	public String type() {
		return this.type;
	}

	public String icon() {
		return this.icon;
	}

	public String when() {
		return this.when;
	}

	public java.util.List<Property> propertyList() {
		return this.propertyList;
	}

	public Operation name(String name) {
		this.name = name;
		return this;
	}

	public Operation title(String title) {
		this.title = title;
		return this;
	}

	public Operation type(String type) {
		this.type = type;
		return this;
	}

	public Operation icon(String icon) {
		this.icon = icon;
		return this;
	}

	public Operation when(String when) {
		this.when = when;
		return this;
	}

	public Operation propertyList(java.util.List<Property> propertyList) {
		this.propertyList = propertyList;
		return this;
	}
}