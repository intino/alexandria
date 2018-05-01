package io.intino.konos.alexandria.ui.model.views.container;

import io.intino.konos.alexandria.ui.model.Mold;

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
