package io.intino.alexandria.ui.documentation;

public class Item {
	private String label;

	public String label() {
		return label;
	}

	public Item label(String label) {
		this.label = label;
		return this;
	}
}
