package io.intino.konos.alexandria.framework.box.model.catalog.events;

public class OnClickRecord {
	private OpenPanel openPanel;
	private OpenDialog openDialog;

	public OpenPanel openPanel() {
		return openPanel;
	}

	public OnClickRecord openPanel(OpenPanel openPanel) {
		this.openPanel = openPanel;
		return this;
	}

	public OpenDialog openDialog() {
		return openDialog;
	}

	public OnClickRecord openDialog(OpenDialog openDialog) {
		this.openDialog = openDialog;
		return this;
	}
}
