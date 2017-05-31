package io.intino.konos.server.activity.dialogs.schemas;

public class DialogInput implements java.io.Serializable {

	private String name = "";
	private String value = "";

	public String name() {
		return this.name;
	}

	public String value() {
		return this.value;
	}

	public DialogInput name(String name) {
		this.name = name;
		return this;
	}

	public DialogInput value(String value) {
		this.value = value;
		return this;
	}
}