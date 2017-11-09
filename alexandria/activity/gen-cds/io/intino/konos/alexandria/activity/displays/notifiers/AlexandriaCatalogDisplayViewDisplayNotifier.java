package io.intino.konos.alexandria.activity.displays.notifiers;

import io.intino.konos.alexandria.activity.schemas.*;

public class AlexandriaCatalogDisplayViewDisplayNotifier extends io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifier {

    public AlexandriaCatalogDisplayViewDisplayNotifier(io.intino.konos.alexandria.activity.displays.AlexandriaDisplay display, io.intino.konos.alexandria.activity.displays.MessageCarrier carrier) {
        super(display, carrier);
    }

	public void refreshView(ElementView value) {
		putToDisplay("refreshView", "value", value);
	}

	public void displayType(String value) {
		putToDisplay("displayType", "value", value);
	}
}
