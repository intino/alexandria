package io.intino.konos.alexandria.ui.model.views.container;

import io.intino.konos.alexandria.ui.model.Panel;

public class PanelContainer extends Container {
	private Panel panel;

	public Panel panel() {
		return panel;
	}

	public PanelContainer panel(Panel panel) {
		this.panel = panel;
		return this;
	}
}
