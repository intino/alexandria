package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.RootNotifier;

public class Root<DN extends RootNotifier, B extends Box> extends Display<DN, B> {

	public Root(B box) {
		super(box);
	}

}