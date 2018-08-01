package io.intino.konos.alexandria.ui.displays.requesters;

import io.intino.konos.alexandria.ui.displays.AlexandriaDisplay;
import io.intino.konos.alexandria.ui.displays.AlexandriaDisplayNotifierProvider;
import io.intino.konos.alexandria.ui.services.push.UIClient;
import io.intino.konos.alexandria.ui.spark.UISparkManager;

public abstract class AlexandriaDelegateDisplayRequester extends AlexandriaDisplayRequester {

	public AlexandriaDelegateDisplayRequester(UISparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	public <D extends AlexandriaDisplay> D personifiedDisplay() {
		String displayId = manager.fromPath("personifiedDisplay", String.class);
		UIClient client = manager.currentClient();
		return client == null ? null : client.soul().get(displayId);
	}

}
