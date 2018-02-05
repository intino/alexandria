package io.intino.konos.alexandria.activity.model.catalog.views;

import io.intino.konos.alexandria.activity.model.catalog.View;
import io.intino.konos.alexandria.activity.model.Mold;

public class MoldView extends View {
	private Mold mold;

	public Mold mold() {
		return mold;
	}

	public MoldView mold(Mold mold) {
		this.mold = mold;
		return this;
	}
}
