package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.ComponentNotifier;

public abstract class Block<DN extends ComponentNotifier, B extends Box> extends Display<DN, B> {
	private boolean isMoldable;

	protected Block(B box) {
		super(box);
	}

	public boolean isMoldable() {
		return isMoldable;
	}

	public Block isMoldable(boolean value) {
		this.isMoldable = value;
		return this;
	}

}