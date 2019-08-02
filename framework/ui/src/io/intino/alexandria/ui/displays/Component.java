package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.HideListener;
import io.intino.alexandria.ui.displays.events.ShowListener;
import io.intino.alexandria.ui.displays.notifiers.ComponentNotifier;

public abstract class Component<DN extends ComponentNotifier, B extends Box> extends AlexandriaDisplay<DN, B> {
	private String color;
	private boolean visible = false;
	private ShowListener showListener = null;
	private HideListener hideListener = null;

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

	public Component<DN, B> onShow(ShowListener listener) {
		this.showListener = listener;
		return this;
	}

	public Component<DN, B> onHide(HideListener listener) {
		this.hideListener = listener;
		return this;
	}

	public void notifyUser(String message, UserMessage.Type messageType) {
		notifier.userMessage(new UserMessage().message(message).type(messageType));
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean isHidden() {
		return !visible;
	}

	public Component<DN, B> visible(boolean value) {
		updateVisibility(value);
		return this;
	}

	public Component<DN, B> show() {
		updateVisibility(true);
		return this;
	}

	public Component<DN, B> hide() {
		updateVisibility(false);
		return this;
	}

	protected void updateVisible(boolean value) {
		this.visible = value;
	}

	protected void updateVisibility(boolean value) {
		updateVisible(value);
		notifier.refreshVisibility(value);
		notifyVisibility();
	}

	protected void notifyVisibility() {
		if (showListener != null && visible) showListener.accept(new Event(this));
		if (hideListener != null && !visible) hideListener.accept(new Event(this));
	}

}