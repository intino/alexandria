package io.intino.konos.server.activity.dialogs.schemas;

public class DialogInput implements java.io.Serializable {

	private String name = "";
	private String value = "";
	private int position = 0;

	public String name() {
		return this.name;
	}

	public String value() {
		return this.value;
	}

	public int position() {
		return this.position;
	}

	public DialogInput name(String name) {
		this.name = name;
		return this;
	}

	public DialogInput value(String value) {
		this.value = value;
		return this;
	}

	public DialogInput position(int position) {
		this.value = value;
		return this;
	}
}