package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;

public class ValueEvent extends Event {
	private final String value;

	public ValueEvent(Display sender, String value) {
		super(sender);
		this.value = value;
	}

	public String value() {
		return value;
	}

}
