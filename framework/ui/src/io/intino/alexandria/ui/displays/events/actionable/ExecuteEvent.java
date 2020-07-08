package io.intino.alexandria.ui.displays.events.actionable;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.events.Event;

public class ExecuteEvent extends Event {
	private final String option;

	public ExecuteEvent(Display sender, String option) {
		super(sender);
		this.option = option;
	}

	public String option() { return option; }
}
