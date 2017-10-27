package io.intino.konos.server.activity.displays.elements.items;

import io.intino.konos.exceptions.KonosException;
import io.intino.konos.server.activity.displays.DisplayNotifierProvider;
import io.intino.konos.server.activity.spark.ActivitySparkManager;
import io.intino.konos.server.activity.spark.resources.DisplayRequester;

public class PageContainerDisplayRequester extends DisplayRequester {

	public PageContainerDisplayRequester(ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws KonosException {
		PageContainerDisplay display = display();
		if (display == null) return;
		String operation = operation();


	}
}