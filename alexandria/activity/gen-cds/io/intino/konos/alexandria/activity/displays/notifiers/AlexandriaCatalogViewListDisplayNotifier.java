package io.intino.konos.alexandria.activity.displays.notifiers;

import io.intino.konos.alexandria.activity.schemas.*;

public class AlexandriaCatalogViewListDisplayNotifier extends io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifier {

    public AlexandriaCatalogViewListDisplayNotifier(io.intino.konos.alexandria.activity.displays.AlexandriaDisplay display, io.intino.konos.alexandria.activity.displays.MessageCarrier carrier) {
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
