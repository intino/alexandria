package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;

public class SelectEvent extends Event {
	private final Object option;

	public SelectEvent(Display sender, Object option) {
		super(sender);
		this.option = option;
	}

	public <T> T option() {
		return (T) option;
	}

}
