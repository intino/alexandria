package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.DisplayNotifier;

public class PassiveView<DN extends DisplayNotifier, B extends Box> extends Display<DN, B> {

	public PassiveView(B box) {
		super(box);
	}

}