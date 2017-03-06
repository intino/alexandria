package io.intino.konos.server.activity.spark.resources;

import io.intino.konos.server.activity.displays.Display;
import io.intino.konos.server.activity.displays.DisplayNotifierProvider;
import io.intino.konos.server.activity.services.push.ActivityClient;
import io.intino.konos.server.activity.spark.ActivitySparkManager;

public abstract class DisplayRequester extends Resource {

	public DisplayRequester(ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	public <D extends Display> D display() {
		String displayId = manager.fromPath("displayId", String.class);
		ActivityClient client = manager.currentClient();
		return client == null ? null : client.soul().get(displayId);
	}

	public String operation() {
		return manager.fromQuery("operation", String.class);
	}

}
