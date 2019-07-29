package io.intino.alexandria.ui.displays.requesters;

import io.intino.alexandria.ui.Soul;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.services.push.UIClient;
import io.intino.alexandria.ui.services.push.UIMessage;

public abstract class DisplayPushRequester {

	public void execute(UIClient client, UIMessage message) {}

	public <D extends Display> D display(UIClient client, UIMessage message) {
		Soul soul = client != null ? client.soul() : null;
		return soul != null ? soul.displayWithId(message.owner(), message.context(), message.display()) : null;
	}

	public String operation(UIMessage message) {
		return message.operation();
	}

	public String data(UIMessage message) {
		return message.value();
	}

}
