package io.intino.konos.alexandria.activity.schemas;

public class Resource implements java.io.Serializable {

	private String name = "";
	private String value = "";

	public String name() {
		return this.name;
	}

	public String value() {
		return this.value;
	}

	public Resource name(String name) {
		this.name = name;
		return this;
	}

	public Resource value(String value) {
		this.value = value;
		return this;
	}
}