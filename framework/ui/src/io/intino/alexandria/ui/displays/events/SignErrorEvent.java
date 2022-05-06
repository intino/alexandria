package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;

public class SignErrorEvent extends Event {
	private final String code;
	private final String message;

	public SignErrorEvent(Display sender, String code, String message) {
		super(sender);
		this.code = code;
		this.message = message;
	}

	public String code() {
		return code;
	}

	public String message() {
		return message;
	}

}
