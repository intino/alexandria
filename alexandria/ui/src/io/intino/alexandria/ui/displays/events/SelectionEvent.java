package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;

import java.util.List;

public class SelectionEvent extends Event {
	private final List<String> selection;

	public SelectionEvent(Display sender, List<String> selection) {
		super(sender);
		this.selection = selection;
	}

	public String first() { return selection.size() > 0 ? selection.get(0) : null; }

	public List<String> selection() {
		return selection;
	}

}
