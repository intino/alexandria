package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;

public class KeyPressEvent extends Event {
	private final Object value;
	private final String keyCode;

	public KeyPressEvent(Display sender, Object value, String keyCode) {
		super(sender);
		this.value = value;
		this.keyCode = keyCode;
	}

	public <V extends Object> V value() {
		return (V) value;
	}

	public String keyCode() {
		return keyCode;
	}

}
