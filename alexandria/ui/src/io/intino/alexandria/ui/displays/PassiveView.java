package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.PassiveViewNotifier;

public class PassiveView<DN extends PassiveViewNotifier, B extends Box> extends Display<DN, B> {

	public PassiveView(B box) {
		super(box);
	}

}