package io.intino.alexandria.ui.displays.requesters;

import io.intino.alexandria.ui.Soul;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.services.push.UIClient;
import io.intino.alexandria.ui.services.push.UIMessage;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public abstract class DisplayPushRequester {

	public void execute(UIClient client, UIMessage message) {
		Display display = display(client, message);
		if (display == null) return;
		String operation = operation(message);
		if (operation.equals("didMount")) display.didMount();
	}

	public <D extends Display> D display(UIClient client, UIMessage message) {
		Soul soul = client != null ? client.soul() : null;
		return soul != null ? soul.displayWithId(message.owner(), message.context(), message.display()) : null;
	}

	public String operation(UIMessage message) {
		return message.operation();
	}

	public String data(UIMessage message) {
		if (message.value() == null || message.value().equals("null")) return null;
		return URLDecoder.decode(message.value(), StandardCharsets.UTF_8);
	}

}
