package io.intino.konos.server.activity.displays.schemas;

public class Reference implements java.io.Serializable {

	private String name = "";
	private String label = "";
	private java.util.List<ReferenceProperty> referencePropertyList = new java.util.ArrayList<>();

	public String name() {
		return this.name;
	}

	public String label() {
		return this.label;
	}

	public java.util.List<ReferenceProperty> referencePropertyList() {
		return this.referencePropertyList;
	}

	public Reference name(String name) {
		this.name = name;
		return this;
	}

	public Reference label(String label) {
		this.label = label;
		return this;
	}

	public Reference referencePropertyList(java.util.List<ReferenceProperty> referencePropertyList
	) {
		this.referencePropertyList = referencePropertyList;
		return this;
	}
}