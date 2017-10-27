package io.intino.konos.server.activity.displays.catalogs.model.views;

import io.intino.konos.server.activity.displays.catalogs.model.View;
import io.intino.konos.server.activity.displays.molds.model.Mold;

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
