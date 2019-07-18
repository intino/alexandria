package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;

import java.util.List;

public class SelectionEvent extends Event {
	private final List selection;

	public SelectionEvent(Display sender, List selection) {
		super(sender);
		this.selection = selection;
	}

	public <T> T first() { return selection.size() > 0 ? (T) selection.get(0) : null; }

	public <T> List<T> selection() {
		return (List<T>) selection;
	}

}
