package io.intino.konos.alexandria.ui.displays.notifiers;

import io.intino.konos.alexandria.ui.displays.AlexandriaDisplay;
import io.intino.konos.alexandria.ui.displays.AlexandriaDisplayNotifier;
import io.intino.konos.alexandria.ui.displays.MessageCarrier;

public abstract class AlexandriaProxyDisplayNotifier extends AlexandriaDisplayNotifier {

	public AlexandriaProxyDisplayNotifier(AlexandriaDisplay display, MessageCarrier carrier) {
		super(display, carrier);
	}

}