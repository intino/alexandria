package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;

public class SelectEvent extends Event {
	private final String option;

	public SelectEvent(Display sender, String option) {
		super(sender);
		this.option = option;
	}

	public String option() {
		return option;
	}

}
