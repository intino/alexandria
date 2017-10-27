package io.intino.konos.server.activity.displays.schemas;

public class ItemStamp implements java.io.Serializable {

	private String name = "";
	private java.util.List<String> values = new java.util.ArrayList<>();
	private java.util.List<Property> propertyList = new java.util.ArrayList<>();

	public String name() {
		return this.name;
	}

	public java.util.List<String> values() {
		return this.values;
	}

	public java.util.List<Property> propertyList() {
		return this.propertyList;
	}

	public ItemStamp name(String name) {
		this.name = name;
		return this;
	}

	public ItemStamp values(java.util.List<String> values) {
		this.values = values;
		return this;
	}

	public ItemStamp propertyList(java.util.List<Property> propertyList) {
		this.propertyList = propertyList;
		return this;
	}
}