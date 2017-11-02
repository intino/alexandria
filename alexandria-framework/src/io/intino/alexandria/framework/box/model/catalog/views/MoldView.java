package io.intino.alexandria.framework.box.model.catalog.views;

import io.intino.alexandria.framework.box.model.catalog.View;
import io.intino.alexandria.framework.box.model.Mold;

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
