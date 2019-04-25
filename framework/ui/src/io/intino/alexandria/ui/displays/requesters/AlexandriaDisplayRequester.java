package io.intino.alexandria.ui.displays.requesters;

import io.intino.alexandria.ui.displays.AlexandriaDisplay;
import io.intino.alexandria.ui.displays.AlexandriaDisplayNotifierProvider;
import io.intino.alexandria.ui.services.push.UIClient;
import io.intino.alexandria.ui.spark.UISparkManager;
import io.intino.alexandria.ui.spark.resources.Resource;

public abstract class AlexandriaDisplayRequester extends Resource {

	public AlexandriaDisplayRequester(UISparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	public <D extends AlexandriaDisplay> D display() {
		String displayId = manager.fromPath("displayId", String.class);
		UIClient client = manager.currentClient();
		return client == null ? null : client.soul().get(displayId);
	}

	public String operation() {
		return manager.fromQuery("operation", String.class);
	}

}
