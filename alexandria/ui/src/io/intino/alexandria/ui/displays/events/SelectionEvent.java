package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;

import java.util.List;

public class SelectionEvent extends Event {
	private final List<Object> items;

	public SelectionEvent(Display sender, List<Object> items) {
		super(sender);
		this.items = items;
	}

	public <T> List<T> items() {
		return (List<T>) items;
	}

}
