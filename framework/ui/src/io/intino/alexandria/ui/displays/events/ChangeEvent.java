package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;

public class ChangeEvent extends Event {
	private final Object value;

	public ChangeEvent(Display sender, Object value) {
		super(sender);
		this.value = value;
	}

	public <V extends Object> V value() {
		return (V) value;
	}

}
