package io.intino.konos.server.activity.displays.elements.model.renders;

import io.intino.konos.server.activity.displays.elements.model.ElementRender;
import io.intino.konos.server.activity.displays.layouts.model.ElementOption;
import io.intino.konos.server.activity.displays.molds.model.Mold;

public class RenderMold extends ElementRender {
	private Mold mold;

	public RenderMold(ElementOption option) {
		super(option);
	}

	public Mold mold() {
		return mold;
	}

	public RenderMold mold(Mold mold) {
		this.mold = mold;
		return this;
	}
}
