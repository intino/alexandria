package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.ComponentNotifier;

public abstract class Mold<DN extends ComponentNotifier, B extends Box> extends Display<DN, B> {

	protected Mold(B box) {
		super(box);
	}

}