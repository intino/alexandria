package io.intino.alexandria.ui.displays.notifiers;

import io.intino.alexandria.rest.pushservice.MessageCarrier;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.UserMessage;

public class ComponentNotifier extends DisplayNotifier {

	public ComponentNotifier(Display display, MessageCarrier carrier) {
		super(display, carrier);
	}

	public void refreshLoading(Boolean value) {
		putToDisplay("refreshLoading", "v", value);
	}

	public void userMessage(UserMessage value) {
		putToDisplay("userMessage", "v", value);
	}

	public void refreshVisibility(boolean value) {
		putToDisplay("refreshVisibility", "v", value);
	}

}
