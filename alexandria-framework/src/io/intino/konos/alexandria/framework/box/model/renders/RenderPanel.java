package io.intino.konos.alexandria.framework.box.model.renders;

import io.intino.konos.alexandria.framework.box.model.ElementRender;
import io.intino.konos.alexandria.framework.box.model.layout.ElementOption;
import io.intino.konos.alexandria.framework.box.model.Panel;

public class RenderPanel extends ElementRender {
	private Panel panel;
	private Object object = null;

	public RenderPanel(ElementOption option) {
		super(option);
	}

	public Panel panel() {
		return panel;
	}

	public RenderPanel panel(Panel panel) {
		this.panel = panel;
		return this;
	}

	public Object item() {
		return object;
	}

	public RenderPanel item(Object object) {
		this.object = object;
		return this;
	}
}
