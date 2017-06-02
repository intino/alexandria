package io.intino.konos.server.activity.dialogs.schemas;

public class DialogInputResourceIdentifier implements java.io.Serializable {

	private String input = "";
	private int position;

	public String input() {
		return this.input;
	}

	public int position() {
		return this.position;
	}

	public DialogInputResourceIdentifier input(String input) {
		this.input = input;
		return this;
	}

	public DialogInputResourceIdentifier position(int position) {
		this.position = position;
		return this;
	}

}