package io.intino.konos.alexandria.activity.schemas;

public class Property implements java.io.Serializable {

	private String name = "";
	private String value = "";

	public String name() {
		return this.name;
	}

	public String value() {
		return this.value;
	}

	public Property name(String name) {
		this.name = name;
		return this;
	}

	public Property value(String value) {
		this.value = value;
		return this;
	}
}