package io.intino.alexandria.ui.model.catalog.events;

public class OnClickItem {
	private OpenPanel openPanel;
	private OpenDialog openDialog;
	private OpenCatalog openCatalog;

	public OpenPanel openPanel() {
		return openPanel;
	}

	public OnClickItem openPanel(OpenPanel openPanel) {
		this.openPanel = openPanel;
		return this;
	}

	public OpenDialog openDialog() {
		return openDialog;
	}

	public OnClickItem openDialog(OpenDialog openDialog) {
		this.openDialog = openDialog;
		return this;
	}

	public OpenCatalog openCatalog() {
		return openCatalog;
	}

	public OnClickItem openCatalog(OpenCatalog openCatalog) {
		this.openCatalog = openCatalog;
		return this;
	}
}
