package io.intino.konos.alexandria.framework.box.model.renders;

import io.intino.konos.alexandria.framework.box.model.ElementRender;
import io.intino.konos.alexandria.framework.box.model.layout.ElementOption;
import io.intino.konos.alexandria.framework.box.model.Mold;

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
