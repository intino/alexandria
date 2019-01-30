package io.intino.alexandria.ui.displays.requesters;

import io.intino.alexandria.ui.displays.notifiers.DisplayNotifierProvider;
import io.intino.alexandria.ui.spark.UISparkManager;

public abstract class ComponentRequester extends DisplayRequester {

	public ComponentRequester(UISparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

}
