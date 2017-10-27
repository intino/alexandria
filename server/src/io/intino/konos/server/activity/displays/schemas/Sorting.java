package io.intino.konos.server.activity.displays.schemas;

public class Sorting implements java.io.Serializable {

	private String name = "";
	private String label = "";
	private String mode = "";

	public String name() {
		return this.name;
	}

	public String label() {
		return this.label;
	}

	public String mode() {
		return this.mode;
	}

	public Sorting name(String name) {
		this.name = name;
		return this;
	}

	public Sorting label(String label) {
		this.label = label;
		return this;
	}

	public Sorting mode(String mode) {
		this.mode = mode;
		return this;
	}
}