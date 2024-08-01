package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.actionable.ExecuteEvent;
import io.intino.alexandria.ui.displays.events.actionable.ExecuteListener;
import io.intino.alexandria.ui.displays.notifiers.SetDarkModeNotifier;

public class SetDarkMode<DN extends SetDarkModeNotifier, B extends Box> extends AbstractSetDarkMode<DN, B> {
	private ExecuteListener executeListener;

	public SetDarkMode(B box) {
		super(box);
	}

	public SetDarkMode<DN, B> onExecute(ExecuteListener listener) {
		this.executeListener = listener;
		return this;
	}

	public void execute() {
		notifier.updateMode();
		soul().displays(SetLightMode.class).forEach(d -> d.visible(true));
		soul().displays(SetDarkMode.class).forEach(d -> d.visible(false));
		executeListener.accept(new ExecuteEvent(this, "dark"));
	}

}