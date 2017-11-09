package io.intino.konos.alexandria.activity.model.renders;

import io.intino.konos.alexandria.activity.model.ElementRender;
import io.intino.konos.alexandria.activity.model.Panel;

public class RenderPanel extends ElementRender {
	private Panel panel;
	private Object object = null;

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
