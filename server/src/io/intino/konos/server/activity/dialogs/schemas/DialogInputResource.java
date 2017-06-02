package io.intino.konos.server.activity.dialogs.schemas;

public class DialogInputResource implements java.io.Serializable {

	private String input = "";
	private Resource resource;

	public String input() {
		return this.input;
	}

	public Resource resource() {
		return this.resource;
	}

	public DialogInputResource input(String input) {
		this.input = input;
		return this;
	}

	public DialogInputResource resource(Resource resource) {
		this.resource = resource;
		return this;
	}

}