package io.intino.konos.server.activity.displays.catalogs.views;

import io.intino.konos.server.activity.displays.schemas.Reference;

public class CatalogViewListDisplayNotifier extends io.intino.konos.server.activity.displays.DisplayNotifier {

    public CatalogViewListDisplayNotifier(io.intino.konos.server.activity.displays.Display display, io.intino.konos.server.activity.displays.MessageCarrier carrier) {
        super(display, carrier);
    }

	public void refreshTarget(String value) {
		putToDisplay("refreshTarget", "value", value);
	}

	public void refreshViewList(java.util.List<Reference> value) {
		putToDisplay("refreshViewList", "value", value);
	}

	public void refreshSelectedView(String value) {
		putToDisplay("refreshSelectedView", "value", value);
	}
}
