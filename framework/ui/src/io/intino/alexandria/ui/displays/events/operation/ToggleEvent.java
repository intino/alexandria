package io.intino.alexandria.ui.displays.events.operation;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.switches.State;
import io.intino.alexandria.ui.displays.events.Event;

public class ToggleEvent extends Event {
	private final State state;

	public ToggleEvent(Display sender, State state) {
		super(sender);
		this.state = state;
	}

	public State state() { return state; }
}
