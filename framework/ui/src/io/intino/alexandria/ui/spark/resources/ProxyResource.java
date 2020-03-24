package io.intino.alexandria.ui.spark.resources;

import io.intino.alexandria.ui.Soul;
import io.intino.alexandria.ui.displays.notifiers.DisplayNotifierProvider;
import io.intino.alexandria.ui.services.push.UIClient;
import io.intino.alexandria.ui.services.push.UISession;
import io.intino.alexandria.ui.spark.UISparkManager;

public abstract class ProxyResource extends Resource {

	public ProxyResource(UISparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	protected Soul soul() {
		UIClient client = client();
		return client == null ? null : client.soul();
	}

	protected UIClient client() {
		return manager.client(manager.fromQuery("client"));
	}

	protected UISession session() {
		return manager.session(manager.fromQuery("session"));
	}

}

