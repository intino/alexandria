package io.intino.konos.alexandria.foundation.activity.schemas;

public class DialogInput implements java.io.Serializable {

	private String path = "";
	private String value = "";

	public String path() {
		return this.path;
	}

	public DialogInput path(String path) {
		this.path = path;
		return this;
	}

	public String value() {
		return this.value;
	}

	public DialogInput value(String value) {
		this.value = value;
		return this;
	}

}