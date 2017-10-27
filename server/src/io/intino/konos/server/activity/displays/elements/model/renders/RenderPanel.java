package io.intino.konos.server.activity.displays.elements.model.renders;

import io.intino.konos.server.activity.displays.elements.model.ElementRender;
import io.intino.konos.server.activity.displays.layouts.model.ElementOption;
import io.intino.konos.server.activity.displays.panels.model.Panel;

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
