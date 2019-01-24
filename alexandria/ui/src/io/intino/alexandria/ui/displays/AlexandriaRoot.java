package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaRootNotifier;

public class AlexandriaRoot<DN extends AlexandriaRootNotifier, B extends Box> extends AlexandriaDisplay<DN, B> {

	public AlexandriaRoot(B box) {
		super(box);
	}

}