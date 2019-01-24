package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.AlexandriaComponent;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaTabNotifier;

public class AlexandriaTab<B extends Box> extends AlexandriaComponent<AlexandriaTabNotifier, B> {

	public AlexandriaTab(B box) {
		super(box);
	}

}