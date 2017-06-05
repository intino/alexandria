package io.intino.konos.server.activity.dialogs.schemas;

public class DialogInputValueIdentifier implements java.io.Serializable {

	private String input = "";
	private int position;

	public String input() {
		return this.input;
	}

	public int position() {
		return this.position;
	}

	public DialogInputValueIdentifier input(String input) {
		this.input = input;
		return this;
	}

	public DialogInputValueIdentifier position(int position) {
		this.position = position;
		return this;
	}

}