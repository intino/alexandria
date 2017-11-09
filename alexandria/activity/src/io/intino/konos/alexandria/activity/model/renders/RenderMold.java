package io.intino.konos.alexandria.activity.model.renders;

import io.intino.konos.alexandria.activity.model.ElementRender;
import io.intino.konos.alexandria.activity.model.Mold;

public class RenderMold extends ElementRender {
	private Mold mold;

	public Mold mold() {
		return mold;
	}

	public RenderMold mold(Mold mold) {
		this.mold = mold;
		return this;
	}
}
