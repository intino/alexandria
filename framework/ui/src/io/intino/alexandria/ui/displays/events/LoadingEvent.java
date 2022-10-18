package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;

public class LoadingEvent extends Event {
	private final boolean loading;

	public LoadingEvent(Display sender, boolean loading) {
		super(sender);
		this.loading = loading;
	}

	public boolean loading() {
		return loading;
	}
}
