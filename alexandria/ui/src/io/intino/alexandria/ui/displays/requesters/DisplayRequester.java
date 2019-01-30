package io.intino.alexandria.ui.displays.requesters;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.notifiers.DisplayNotifierProvider;
import io.intino.alexandria.ui.services.push.UIClient;
import io.intino.alexandria.ui.spark.UISparkManager;
import io.intino.alexandria.ui.spark.resources.Resource;

public abstract class DisplayRequester extends Resource {

	public DisplayRequester(UISparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	public <D extends Display> D display() {
		String displayId = manager.fromPath("displayId", String.class);
		UIClient client = manager.currentClient();
		return client == null ? null : client.soul().get(displayId);
	}

	public String operation() {
		return manager.fromQuery("operation", String.class);
	}

}
