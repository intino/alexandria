package io.intino.konos.alexandria.activity.schemas;

public class DialogInputResource implements java.io.Serializable {

	private String path = "";
	private io.intino.konos.Resource file;

	public String path() {
		return this.path;
	}

	public io.intino.konos.Resource file() {
		return this.file;
	}

	public DialogInputResource path(String path) {
		this.path = path;
		return this;
	}

	public DialogInputResource file(io.intino.konos.Resource file) {
		this.file = file;
		return this;
	}
}