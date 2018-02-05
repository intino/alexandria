package io.intino.konos.alexandria.activity.model.renders;

import io.intino.konos.alexandria.activity.model.ElementRender;
import io.intino.konos.alexandria.activity.model.Panel;

import java.util.List;

public class RenderPanels extends ElementRender {
	private List<Panel> panels;

	public List<Panel> panels() {
		return panels;
	}

	public RenderPanels panels(List<Panel> panels) {
		this.panels = panels;
		return this;
	}
}
