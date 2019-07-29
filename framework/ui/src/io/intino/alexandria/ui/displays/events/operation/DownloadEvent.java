package io.intino.alexandria.ui.displays.events.operation;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.events.Event;

public class DownloadEvent extends Event {
	private final String option;

	public DownloadEvent(Display sender, String option) {
		super(sender);
		this.option = option;
	}

	public String option() { return option; }
}
