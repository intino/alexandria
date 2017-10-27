package io.intino.konos.server.activity.displays.catalogs;

public class TemporalTimeCatalogDisplayNotifier extends TemporalCatalogDisplayNotifier {

	public TemporalTimeCatalogDisplayNotifier(io.intino.konos.server.activity.displays.Display display, io.intino.konos.server.activity.displays.MessageCarrier carrier) {
		super(display, carrier);
	}

	public void showTimeNavigator() {
		putToDisplay("showTimeNavigator");
	}

	public void hideTimeNavigator() {
		putToDisplay("hideTimeNavigator");
	}

}