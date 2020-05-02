package io.intino.alexandria.ui.displays;

import io.intino.alexandria.Json;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.HideListener;
import io.intino.alexandria.ui.displays.events.ShowListener;
import io.intino.alexandria.ui.displays.notifiers.ComponentNotifier;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class Component<DN extends ComponentNotifier, B extends Box> extends AlexandriaDisplay<DN, B> {
	private String color;
	private boolean visible = false;
	private List<ShowListener> showListeners = new ArrayList<>();
	private List<HideListener> hideListeners = new ArrayList<>();

	protected Component(B box) {
		super(box);
	}

	public String color() {
		return color;
	}

	public Component<DN, B> color(String color) {
		this._color(color);
		notifier.refreshColor(color);
		return this;
	}

	public Component<DN, B> onShow(ShowListener listener) {
		this.showListeners.add(listener);
		return this;
	}

	public Component<DN, B> onHide(HideListener listener) {
		this.hideListeners.add(listener);
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

	public <T extends Component> T visible(boolean value) {
		updateVisibility(value);
		return (T) this;
	}

	public Component<DN, B> show() {
		updateVisibility(true);
		return this;
	}

	public Component<DN, B> hide() {
		updateVisibility(false);
		return this;
	}

	public <T> T trace(Class<T> clazz) {
		String value = session().client().cookie(id());
		return value != null ? Json.fromString(URLDecoder.decode(value, StandardCharsets.UTF_8), clazz) : null;
	}

	protected Component _color(String color) {
		this.color = color;
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
		if (visible) showListeners.forEach(l -> l.accept(new Event(this)));
		if (!visible) hideListeners.forEach(l -> l.accept(new Event(this)));
	}

}