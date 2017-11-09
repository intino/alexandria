package io.intino.konos.alexandria.activity.schemas;

public class ReferenceProperty implements java.io.Serializable {

	private String name = "";
	private String value = "";

	public String name() {
		return this.name;
	}

	public String value() {
		return this.value;
	}

	public ReferenceProperty name(String name) {
		this.name = name;
		return this;
	}

	public ReferenceProperty value(String value) {
		this.value = value;
		return this;
	}
}