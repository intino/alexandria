package io.intino.alexandria.ui.model.view.container;

import io.intino.alexandria.ui.model.Panel;

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
