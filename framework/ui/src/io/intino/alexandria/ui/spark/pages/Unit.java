package io.intino.alexandria.ui.spark.pages;

public class Unit {
	public String name;
	public String url;

	public Unit(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public String name() {
		return name;
	}

	public Unit name(String name) {
		this.name = name;
		return this;
	}

	public String url() {
		return url;
	}

	public Unit url(String url) {
		this.url = url;
		return this;
	}
}
