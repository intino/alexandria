package io.intino.alexandria.drivers.program;

import java.io.InputStream;

public class Resource {
	private String name;
	private InputStream content;

	public String name() {
		return name;
	}

	public Resource name(String name) {
		this.name = name;
		return this;
	}

	public InputStream content() {
		return content;
	}

	public Resource content(InputStream content) {
		this.content = content;
		return this;
	}
}
