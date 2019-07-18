package io.intino.alexandria.ui.displays.requesters;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.notifiers.DisplayNotifierProvider;
import io.intino.alexandria.ui.spark.UISparkManager;

public abstract class DisplayProxyRequester extends DisplayRequester {

	public DisplayProxyRequester(UISparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	public <D extends Display> D personifiedDisplay() {
		return display(manager.fromPath("personifiedDisplay", String.class));
	}

}
