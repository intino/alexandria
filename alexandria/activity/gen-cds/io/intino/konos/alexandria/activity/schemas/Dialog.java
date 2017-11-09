package io.intino.konos.alexandria.activity.schemas;

public class Dialog implements java.io.Serializable {

	private String label = "";
	private String description = "";
	private String definition = "";

	public String label() {
		return this.label;
	}

	public String description() {
		return this.description;
	}

	public String definition() {
		return this.definition;
	}

	public Dialog label(String label) {
		this.label = label;
		return this;
	}

	public Dialog description(String description) {
		this.description = description;
		return this;
	}

	public Dialog definition(String definition) {
		this.definition = definition;
		return this;
	}
}