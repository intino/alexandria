package io.intino.konos.alexandria.activity.displays.notifiers;

import io.intino.konos.alexandria.activity.schemas.*;

public class AlexandriaTemporalTimeCatalogDisplayNotifier extends io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifier {

    public AlexandriaTemporalTimeCatalogDisplayNotifier(io.intino.konos.alexandria.activity.displays.AlexandriaDisplay display, io.intino.konos.alexandria.activity.displays.MessageCarrier carrier) {
        super(display, carrier);
    }

	public void refreshCatalog(Catalog value) {
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

	public void showTimeNavigator() {
		putToDisplay("showTimeNavigator");
	}

	public void hideTimeNavigator() {
		putToDisplay("hideTimeNavigator");
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
