package io.intino.konos.alexandria.activity.schemas;

public class DialogInput implements java.io.Serializable {

	private String path = "";
	private String value = "";

	public String path() {
		return this.path;
	}

	public String value() {
		return this.value;
	}

	public DialogInput path(String path) {
		this.path = path;
		return this;
	}

	public DialogInput value(String value) {
		this.value = value;
		return this;
	}
}