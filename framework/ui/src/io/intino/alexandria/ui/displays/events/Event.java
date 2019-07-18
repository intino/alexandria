package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;

public class Event {
	private final Display sender;

	public Event(Display sender) {
		this.sender = sender;
	}

	public <D extends Display> D sender() {
		return (D) sender;
	}

}
