package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.DesktopNotifier;

public class Desktop<DN extends DesktopNotifier, B extends Box> extends Display<DN, B> {

	public Desktop(B box) {
		super(box);
	}

}