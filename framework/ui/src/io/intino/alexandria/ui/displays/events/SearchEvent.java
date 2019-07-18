package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;

public class SearchEvent extends Event {
	private final String condition;

	public SearchEvent(Display sender, String condition) {
		super(sender);
		this.condition = condition;
	}

	public String condition() {
		return condition;
	}

}
