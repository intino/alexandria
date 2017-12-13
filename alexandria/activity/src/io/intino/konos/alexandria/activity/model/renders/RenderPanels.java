package io.intino.konos.alexandria.activity.model.renders;

import io.intino.konos.alexandria.activity.model.ElementRender;
import io.intino.konos.alexandria.activity.model.Panel;

import java.util.List;

public class RenderPanels extends ElementRender {
	private List<Panel> panels;
	private Object object = null;

	public List<Panel> panels() {
		return panels;
	}

	public RenderPanels panels(List<Panel> panels) {
		this.panels = panels;
		return this;
	}

	public Object item() {
		return object;
	}

	public RenderPanels item(Object object) {
		this.object = object;
		return this;
	}
}
