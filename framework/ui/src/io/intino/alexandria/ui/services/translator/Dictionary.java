package io.intino.alexandria.ui.services.translator;

import java.util.HashMap;

public class Dictionary extends HashMap<String, String> {
	private String language;

	public String language() {
		return this.language;
	}

	public Dictionary language(String language) {
		this.language = language;
		return this;
	}
}
