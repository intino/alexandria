package io.intino.konos.alexandria.activity.displays.notifiers;

import io.intino.konos.alexandria.activity.schemas.*;

public class AlexandriaItemDisplayNotifier extends io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifier {

    public AlexandriaItemDisplayNotifier(io.intino.konos.alexandria.activity.displays.AlexandriaDisplay display, io.intino.konos.alexandria.activity.displays.MessageCarrier carrier) {
        super(display, carrier);
    }

	public void refresh(ItemRefreshInfo value) {
		putToDisplay("refresh", "value", value);
	}

	public void refreshMode(String value) {
		putToDisplay("refreshMode", "value", value);
	}

	public void refreshEmptyMessage(String value) {
		putToDisplay("refreshEmptyMessage", "value", value);
	}
}
