package io.intino.konos.alexandria.activity.box.spark.resources;

import io.intino.konos.alexandria.activity.box.displays.AlexandriaDisplay;
import io.intino.konos.alexandria.activity.box.displays.AlexandriaDisplayNotifierProvider;
import io.intino.konos.alexandria.activity.box.services.push.ActivityClient;
import io.intino.konos.alexandria.activity.box.spark.ActivitySparkManager;

public abstract class DisplayRequester extends Resource {

	public DisplayRequester(ActivitySparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	public <D extends AlexandriaDisplay> D display() {
		String displayId = manager.fromPath("displayId", String.class);
		ActivityClient client = manager.currentClient();
		return client == null ? null : client.soul().get(displayId);
	}

	public String operation() {
		return manager.fromQuery("operation", String.class);
	}

}
