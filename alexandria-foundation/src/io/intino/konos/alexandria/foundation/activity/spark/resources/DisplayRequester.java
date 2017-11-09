package io.intino.konos.alexandria.foundation.activity.spark.resources;

import io.intino.konos.alexandria.foundation.activity.displays.AlexandriaDisplay;
import io.intino.konos.alexandria.foundation.activity.displays.AlexandriaDisplayNotifierProvider;
import io.intino.konos.alexandria.foundation.activity.services.push.ActivityClient;
import io.intino.konos.alexandria.foundation.activity.spark.ActivitySparkManager;

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
