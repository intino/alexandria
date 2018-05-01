package io.intino.konos.alexandria.ui.model.catalog.events;

public class OnClickRecord {
	private OpenPanel openPanel;
	private OpenDialog openDialog;
	private OpenCatalog openCatalog;

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

	public OpenCatalog openCatalog() {
		return openCatalog;
	}

	public OnClickRecord openCatalog(OpenCatalog openCatalog) {
		this.openCatalog = openCatalog;
		return this;
	}
}
