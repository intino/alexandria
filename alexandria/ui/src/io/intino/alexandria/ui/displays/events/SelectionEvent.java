package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;

import java.util.List;

public class SelectionEvent extends Event {
	private final List<String> items;

	public SelectionEvent(Display sender, List<String> items) {
		super(sender);
		this.items = items;
	}

	public String item() { return items.size() > 0 ? items.get(0) : null; }

	public List<String> items() {
		return items;
	}

}
