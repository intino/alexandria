package io.intino.konos.server.activity.displays.schemas;

public class DialogInputResource implements java.io.Serializable {

	private String path = "";
	private Resource resource;

	public String path() {
		return this.path;
	}

	public DialogInputResource path(String path) {
		this.path = path;
		return this;
	}

	public Resource resource() {
		return this.resource;
	}

	public DialogInputResource resource(Resource resource) {
		this.resource = resource;
		return this;
	}

}