package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.SetLightModeNotifier;

public class SetLightMode<DN extends SetLightModeNotifier, B extends Box> extends AbstractSetLightMode<DN, B> {

	public SetLightMode(B box) {
		super(box);
	}

	public void execute() {
		notifier.updateMode();
		soul().displays(SetLightMode.class).forEach(d -> d.visible(false));
		soul().displays(SetDarkMode.class).forEach(d -> d.visible(true));
	}

}