package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;

public class ReadonlyEvent extends Event {
	private final boolean readonly;

	public ReadonlyEvent(Display sender, boolean readonly) {
		super(sender);
		this.readonly = readonly;
	}

	public boolean readonly() {
		return readonly;
	}
}
