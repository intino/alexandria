package io.intino.konos.server.activity.displays.schemas;

public class Scale implements java.io.Serializable {

	private String name = "";
	private String label = "";
	private String symbol = "";

	public String name() {
		return this.name;
	}

	public String label() {
		return this.label;
	}

	public String symbol() {
		return this.symbol;
	}

	public Scale name(String name) {
		this.name = name;
		return this;
	}

	public Scale label(String label) {
		this.label = label;
		return this;
	}

	public Scale symbol(String symbol) {
		this.symbol = symbol;
		return this;
	}
}