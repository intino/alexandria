package io.intino.konos.alexandria.ui.model.catalog.arrangement;

public class Group {
	private String label;
	private int countObjects;

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

	public int countObjects() {
		return countObjects;
	}

	public Group countObjects(int countObjects) {
		this.countObjects = countObjects;
		return this;
	}

	public static String name(String name) {
		return name.replaceAll(AlphaAndDigits,"");
	}
}
