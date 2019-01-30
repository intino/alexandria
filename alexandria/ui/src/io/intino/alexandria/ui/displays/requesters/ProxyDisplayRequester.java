package io.intino.alexandria.ui.displays.requesters;

import io.intino.alexandria.ui.displays.AlexandriaDisplay;
import io.intino.alexandria.ui.displays.notifiers.DisplayNotifierProvider;
import io.intino.alexandria.ui.services.push.UIClient;
import io.intino.alexandria.ui.spark.UISparkManager;

public abstract class AlexandriaProxyDisplayRequester extends AlexandriaDisplayRequester {

	public AlexandriaProxyDisplayRequester(UISparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	public <D extends AlexandriaDisplay> D personifiedDisplay() {
		String displayId = manager.fromQuery("personifiedDisplay", String.class);
		UIClient client = manager.client(manager.fromQuery("client", String.class));
		return client == null ? null : client.soul().get(displayId);
	}

}
