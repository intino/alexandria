package io.intino.konos.alexandria.activity.displays.notifiers;

import io.intino.konos.alexandria.activity.schemas.*;

public class AlexandriaTemporalRangeCatalogDisplayNotifier extends io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifier {

    public AlexandriaTemporalRangeCatalogDisplayNotifier(io.intino.konos.alexandria.activity.displays.AlexandriaDisplay display, io.intino.konos.alexandria.activity.displays.MessageCarrier carrier) {
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

	public void showTimeRangeNavigator() {
		putToDisplay("showTimeRangeNavigator");
	}

	public void hideTimeRangeNavigator() {
		putToDisplay("hideTimeRangeNavigator");
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
