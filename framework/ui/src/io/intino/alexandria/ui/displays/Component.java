package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.ComponentNotifier;

public abstract class Component<DN extends ComponentNotifier, B extends Box> extends Display<DN, B> {
	private String color;

	protected Component(B box) {
		super(box);
	}

	public String color() {
		return color;
	}

	public Component color(String color) {
		this.color = color;
		return this;
	}

	public void notifyUser(String message, UserMessage.Type messageType) {
		notifier.userMessage(new UserMessage().message(message).type(messageType));
	}
}