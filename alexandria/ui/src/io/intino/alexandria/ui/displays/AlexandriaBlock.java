package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaBlockNotifier;

public abstract class AlexandriaBlock<DN extends AlexandriaBlockNotifier, B extends Box> extends AlexandriaDisplay<DN, B> {

	protected AlexandriaBlock(B box) {
		super(box);
	}

}