package io.intino.konos.alexandria.activity.schemas;

public class Stamp implements java.io.Serializable {

	private String name = "";
	private String label = "";
	private Boolean editable = true;
	private String shape = "";
	private String layout = "";
	private Integer height = 0;
	private java.util.List<Property> propertyList = new java.util.ArrayList<>();

	public String name() {
		return this.name;
	}

	public String label() {
		return this.label;
	}

	public Boolean editable() {
		return this.editable;
	}

	public String shape() {
		return this.shape;
	}

	public String layout() {
		return this.layout;
	}

	public Integer height() {
		return this.height;
	}

	public java.util.List<Property> propertyList() {
		return this.propertyList;
	}

	public Stamp name(String name) {
		this.name = name;
		return this;
	}

	public Stamp label(String label) {
		this.label = label;
		return this;
	}

	public Stamp editable(Boolean editable) {
		this.editable = editable;
		return this;
	}

	public Stamp shape(String shape) {
		this.shape = shape;
		return this;
	}

	public Stamp layout(String layout) {
		this.layout = layout;
		return this;
	}

	public Stamp height(Integer height) {
		this.height = height;
		return this;
	}

	public Stamp propertyList(java.util.List<Property> propertyList) {
		this.propertyList = propertyList;
		return this;
	}
}