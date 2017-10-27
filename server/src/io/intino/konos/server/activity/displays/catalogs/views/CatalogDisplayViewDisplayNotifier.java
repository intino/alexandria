package io.intino.konos.server.activity.displays.catalogs.views;

import io.intino.konos.server.activity.displays.schemas.ElementView;

public class CatalogDisplayViewDisplayNotifier extends io.intino.konos.server.activity.displays.DisplayNotifier {

    public CatalogDisplayViewDisplayNotifier(io.intino.konos.server.activity.displays.Display display, io.intino.konos.server.activity.displays.MessageCarrier carrier) {
        super(display, carrier);
    }

	public void displayType(String value) {
		putToDisplay("displayType", "value", value);
	}

	public void refreshView(ElementView value) {
		putToDisplay("refreshView", "value", value);
	}
}
