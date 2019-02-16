package io.intino.alexandria.ui.displays.requesters;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.notifiers.DisplayNotifierProvider;
import io.intino.alexandria.ui.services.push.UIClient;
import io.intino.alexandria.ui.spark.UISparkManager;

public abstract class DisplayProxyRequester extends DisplayRequester {

	public DisplayProxyRequester(UISparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	public <D extends Display> D personifiedDisplay() {
		String displayId = manager.fromQuery("personifiedDisplay", String.class);
		UIClient client = manager.client(manager.fromQuery("client", String.class));
		return client == null ? null : client.soul().get(displayId);
	}

}
