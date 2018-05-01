package io.intino.konos.alexandria.ui.model.catalog.arrangement;

public class Arrangement {
	private String name;
	private String label;

	public String name() {
		return name;
	}

	public Arrangement name(String name) {
		this.name = name;
		return this;
	}

	public String label() {
		return label;
	}

	public Arrangement label(String label) {
		this.label = label;
		return this;
	}

}
