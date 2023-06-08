package io.intino.alexandria.ui.displays.components.sign;

public class SignDocument {
	private final String id;
	private final String url;

	public SignDocument(String id, String url) {
		this.id = id;
		this.url = url;
	}

	public String id() {
		return id;
	}

	public String url() {
		return url;
	}
}
