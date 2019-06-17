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
		return display(manager.fromPath("displayId", String.class));
	}

	public <D extends Display> D display(String displayId) {
		if (displayId == null) return null;
		String[] data = manager.fromPath("displayId", String.class).split(":");
		UIClient client = manager.currentClient();
		return client == null ? null : client.soul().displayWithId(data[0], data[1], data[2]);
	}

	public String operation() {
		return manager.fromQuery("op", String.class);
	}

}
