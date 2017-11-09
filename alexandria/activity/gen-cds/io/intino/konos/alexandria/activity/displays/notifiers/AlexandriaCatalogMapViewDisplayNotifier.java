package io.intino.konos.alexandria.activity.displays.notifiers;

import io.intino.konos.alexandria.activity.schemas.*;

public class AlexandriaCatalogMapViewDisplayNotifier extends io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifier {

    public AlexandriaCatalogMapViewDisplayNotifier(io.intino.konos.alexandria.activity.displays.AlexandriaDisplay display, io.intino.konos.alexandria.activity.displays.MessageCarrier carrier) {
        super(display, carrier);
    }

	public void refreshView(ElementView value) {
		putToDisplay("refreshView", "value", value);
	}

	public void clear() {
		putToDisplay("clear");
	}

	public void refresh(java.util.List<Item> value) {
		putToDisplay("refresh", "value", value);
	}

	public void refreshPageSize(Integer value) {
		putToDisplay("refreshPageSize", "value", value);
	}

	public void refreshItem(Item value) {
		putToDisplay("refreshItem", "value", value);
	}

	public void refreshCount(Integer value) {
		putToDisplay("refreshCount", "value", value);
	}

	public void refreshSelection(java.util.List<String> value) {
		putToDisplay("refreshSelection", "value", value);
	}
}
