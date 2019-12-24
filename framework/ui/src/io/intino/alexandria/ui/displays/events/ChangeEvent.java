package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;

public class ChangeEvent extends Event {
	private final Object value;
	private boolean cancel = false;

	public ChangeEvent(Display sender, Object value) {
		super(sender);
		this.value = value;
	}

	public boolean cancel() {
		return cancel;
	}

	public void cancel(boolean value) {
		cancel = value;
	}

	public <V extends Object> V value() {
		return (V) value;
	}

}
