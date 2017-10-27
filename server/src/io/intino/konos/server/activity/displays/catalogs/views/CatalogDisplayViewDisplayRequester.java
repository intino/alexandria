package io.intino.konos.server.activity.displays.catalogs.views;

import io.intino.konos.exceptions.KonosException;
import io.intino.konos.server.activity.displays.DisplayNotifierProvider;
import io.intino.konos.server.activity.spark.ActivitySparkManager;
import io.intino.konos.server.activity.spark.resources.DisplayRequester;

public class CatalogDisplayViewDisplayRequester extends DisplayRequester {

	public CatalogDisplayViewDisplayRequester(ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws KonosException {
		CatalogDisplayViewDisplay display = display();
		if (display == null) return;
		String operation = operation();


	}
}