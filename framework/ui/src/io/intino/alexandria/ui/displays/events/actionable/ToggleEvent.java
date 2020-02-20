package io.intino.alexandria.ui.displays.events.actionable;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.events.Event;

public class ToggleEvent extends Event {
	private final State state;

	public enum State { On, Off }

	public ToggleEvent(Display sender, boolean state) {
		this(sender, state ? State.On : State.Off);
	}

	public ToggleEvent(Display sender, State state) {
		super(sender);
		this.state = state;
	}

	public State state() { return state; }
}
