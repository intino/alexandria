package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.actionable.ExecuteEvent;
import io.intino.alexandria.ui.displays.events.actionable.ExecuteListener;
import io.intino.alexandria.ui.displays.notifiers.SetLightModeNotifier;

public class SetLightMode<DN extends SetLightModeNotifier, B extends Box> extends AbstractSetLightMode<DN, B> {
	private ExecuteListener executeListener;

	public SetLightMode(B box) {
		super(box);
	}

	public SetLightMode<DN, B> onExecute(ExecuteListener listener) {
		this.executeListener = listener;
		return this;
	}

	public void execute() {
		notifier.updateMode();
		soul().displays(SetLightMode.class).forEach(d -> d.visible(false));
		soul().displays(SetDarkMode.class).forEach(d -> d.visible(true));
		executeListener.accept(new ExecuteEvent(this, "light"));
	}

}