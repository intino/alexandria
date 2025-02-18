package io.intino.alexandria.ui.displays.requesters;

import io.intino.alexandria.ui.displays.notifiers.DisplayNotifierProvider;
import io.intino.alexandria.ui.server.AlexandriaUiManager;

public abstract class ComponentRequester extends DisplayRequester {

	public ComponentRequester(AlexandriaUiManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

}
