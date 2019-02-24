package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;

public class SelectEvent extends Event {
	private final String option;
	private final int position;

	public SelectEvent(Display sender, String option, int position) {
		super(sender);
		this.option = option;
		this.position = position;
	}

	public String option() {
		return option;
	}

	public int position() {
		return position;
	}

}
