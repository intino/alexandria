package io.intino.konos.alexandria.ui.model;

public class Panel extends Element {
	private Layout layout;

	public enum Layout {
		Menu, Tab
	}

	public Layout layout() {
		return layout;
	}

	public Panel layout(Layout layout) {
		this.layout = layout;
		return this;
	}
}
