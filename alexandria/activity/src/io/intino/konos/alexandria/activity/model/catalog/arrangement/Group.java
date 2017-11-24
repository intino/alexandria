package io.intino.konos.alexandria.activity.model.catalog.arrangement;

import java.util.List;

public class Group {
	private String label;
	private List<Object> objects;

	private static final String AlphaAndDigits = "[^a-zA-Z0-9]+";

	public String name() {
		return label.replaceAll(AlphaAndDigits,"");
	}

	public String label() {
		return label;
	}

	public Group label(String label) {
		this.label = label;
		return this;
	}

	public List<Object> objects() {
		return this.objects;
	}

	public Group objects(List<Object> objects) {
		this.objects = objects;
		return this;
	}

	public int countItems() {
		return objects != null ? objects.size() : 0;
	}

	public static String name(String name) {
		return name.replaceAll(AlphaAndDigits,"");
	}
}
