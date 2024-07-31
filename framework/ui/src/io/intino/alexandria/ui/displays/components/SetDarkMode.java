package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.SetDarkModeNotifier;

public class SetDarkMode<DN extends SetDarkModeNotifier, B extends Box> extends AbstractSetDarkMode<DN, B> {

	public SetDarkMode(B box) {
		super(box);
	}

	public void execute() {
		notifier.updateMode();
		soul().displays(SetLightMode.class).forEach(d -> d.visible(true));
		soul().displays(SetDarkMode.class).forEach(d -> d.visible(false));
	}

}