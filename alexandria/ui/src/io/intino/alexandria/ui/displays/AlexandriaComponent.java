package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaComponentNotifier;

public abstract class AlexandriaComponent<DN extends AlexandriaComponentNotifier, B extends Box> extends AlexandriaDisplay<DN, B> {

	protected AlexandriaComponent(B box) {
		super(box);
	}

}