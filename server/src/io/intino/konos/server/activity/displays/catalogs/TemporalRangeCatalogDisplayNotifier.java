package io.intino.konos.server.activity.displays.catalogs;

public class TemporalRangeCatalogDisplayNotifier extends TemporalCatalogDisplayNotifier {

	public TemporalRangeCatalogDisplayNotifier(io.intino.konos.server.activity.displays.Display display, io.intino.konos.server.activity.displays.MessageCarrier carrier) {
		super(display, carrier);
	}

	public void showTimeRangeNavigator() {
		putToDisplay("showTimeRangeNavigator");
	}

	public void hideTimeRangeNavigator() {
		putToDisplay("hideTimeRangeNavigator");
	}

}