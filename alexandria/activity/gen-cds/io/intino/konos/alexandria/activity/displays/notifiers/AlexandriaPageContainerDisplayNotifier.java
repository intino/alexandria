package io.intino.konos.alexandria.activity.displays.notifiers;

import io.intino.konos.alexandria.activity.schemas.*;

public class AlexandriaPageContainerDisplayNotifier extends io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifier {

    public AlexandriaPageContainerDisplayNotifier(io.intino.konos.alexandria.activity.displays.AlexandriaDisplay display, io.intino.konos.alexandria.activity.displays.MessageCarrier carrier) {
        super(display, carrier);
    }

	public void refreshLocation(PageLocation value) {
		putToDisplay("refreshLocation", "value", value);
	}
}
