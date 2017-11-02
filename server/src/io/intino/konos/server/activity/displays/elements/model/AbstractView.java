package io.intino.konos.server.activity.displays.elements.model;

public class AbstractView {
	private String name;
	private String label;

	public String name() {
		return name;
	}

	public AbstractView name(String name) {
		this.name = name;
		return this;
	}

	public String label() {
		return label;
	}

	public AbstractView label(String label) {
		this.label = label;
		return this;
	}
}
