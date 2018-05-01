package io.intino.konos.alexandria.ui.model;

import io.intino.konos.alexandria.ui.services.push.UISession;

public class View {
	private String name;
	private String label;
	private Layout layout;
	private Hidden hidden = null;

	public enum Layout {
		Tab, LeftFixed, RightFixed
	}

	public String name() {
		return name;
	}

	public View name(String name) {
		this.name = name;
		return this;
	}

	public String label() {
		return label;
	}

	public View label(String label) {
		this.label = label;
		return this;
	}

	public Layout layout() {
		return layout;
	}

	public View layout(Layout layout) {
		this.layout = layout;
		return this;
	}

	public boolean hidden(Item item, UISession session) {
		return hidden != null && hidden.hidden(item != null ? item.object() : null, session);
	}

	public View hidden(Hidden hidden) {
		this.hidden = hidden;
		return this;
	}

	public interface Hidden {
		boolean hidden(Object object, UISession session);
	}
}
