package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.BlockNotifier;

public abstract class Block<DN extends BlockNotifier, B extends Box> extends AlexandriaDisplay<DN, B> {

	protected Block(B box) {
		super(box);
	}

}