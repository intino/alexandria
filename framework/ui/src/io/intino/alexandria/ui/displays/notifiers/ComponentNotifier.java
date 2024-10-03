package io.intino.alexandria.ui.displays.notifiers;

import io.intino.alexandria.http.pushservice.MessageCarrier;
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

	public void hideUserMessage() {
		putToDisplay("hideUserMessage");
	}

	public void refreshVisibility(boolean value) {
		putToDisplay("refreshVisibility", "v", value);
	}

	public void refreshColor(String color) {
		putToDisplay("refreshColor", "v", color);
	}

	public void refreshFormat(String format) {
		putToDisplay("refreshFormat", "v", format);
	}

}
