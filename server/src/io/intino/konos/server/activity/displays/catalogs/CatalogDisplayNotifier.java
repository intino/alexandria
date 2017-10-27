package io.intino.konos.server.activity.displays.catalogs;

public class CatalogDisplayNotifier extends io.intino.konos.server.activity.displays.DisplayNotifier {

	public CatalogDisplayNotifier(io.intino.konos.server.activity.displays.Display display, io.intino.konos.server.activity.displays.MessageCarrier carrier) {
		super(display, carrier);
	}

	public void refreshCatalog(io.intino.konos.server.activity.displays.schemas.Catalog value) {
		putToDisplay("refreshCatalog", "value", value);
	}

	public void refreshFiltered(Boolean value) {
		putToDisplay("refreshFiltered", "value", value);
	}

	public void refreshBreadcrumbs(String value) {
		putToDisplay("refreshBreadcrumbs", "value", value);
	}

	public void showDialog() {
		putToDisplay("showDialog");
	}

	public void createPanel(String value) {
		putToDisplay("createPanel", "value", value);
	}

	public void showPanel() {
		putToDisplay("showPanel");
	}

	public void hidePanel() {
		putToDisplay("hidePanel");
	}

}