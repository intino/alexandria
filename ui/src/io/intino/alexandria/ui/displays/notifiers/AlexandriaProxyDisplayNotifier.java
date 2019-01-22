package io.intino.alexandria.ui.displays.notifiers;

import io.intino.alexandria.ui.displays.AlexandriaDisplay;
import io.intino.alexandria.ui.displays.AlexandriaDisplayNotifier;
import io.intino.alexandria.rest.pushservice.MessageCarrier;

public abstract class AlexandriaProxyDisplayNotifier extends AlexandriaDisplayNotifier {

	public AlexandriaProxyDisplayNotifier(AlexandriaDisplay display, MessageCarrier carrier) {
		super(display, carrier);
	}

}