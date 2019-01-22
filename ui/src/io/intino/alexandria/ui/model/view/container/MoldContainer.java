package io.intino.alexandria.ui.model.view.container;

import io.intino.alexandria.ui.model.Mold;

public class MoldContainer extends Container {
	private Mold mold;

	public Mold mold() {
		return mold;
	}

	public MoldContainer mold(Mold mold) {
		this.mold = mold;
		return this;
	}
}
