package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;

public class RemoveItemEvent extends Event {
	private final int index;

	public RemoveItemEvent(Display sender, int index) {
		super(sender);
		this.index = index;
	}

	public int index() {
		return index;
	}

}
