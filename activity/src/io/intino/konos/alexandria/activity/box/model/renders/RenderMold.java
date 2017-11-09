package io.intino.konos.alexandria.activity.box.model.renders;

import io.intino.konos.alexandria.activity.box.model.ElementRender;
import io.intino.konos.alexandria.activity.box.model.Mold;

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
