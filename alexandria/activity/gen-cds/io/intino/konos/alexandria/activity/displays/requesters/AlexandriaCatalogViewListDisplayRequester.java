package io.intino.konos.alexandria.activity.displays.requesters;

import io.intino.konos.alexandria.activity.displays.AlexandriaCatalogViewListDisplay;

import io.intino.konos.alexandria.exceptions.AlexandriaException;
import io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifierProvider;
import io.intino.konos.alexandria.activity.spark.ActivitySparkManager;
import io.intino.konos.alexandria.activity.spark.resources.DisplayRequester;

public class AlexandriaCatalogViewListDisplayRequester extends DisplayRequester {

	public AlexandriaCatalogViewListDisplayRequester(ActivitySparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws AlexandriaException {
		AlexandriaCatalogViewListDisplay display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("selectView")) display.selectView(manager.fromQuery("value", String.class));
	}
}