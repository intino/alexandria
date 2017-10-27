package io.intino.konos.server.activity.displays.catalogs.views;

import io.intino.konos.exceptions.KonosException;
import io.intino.konos.server.activity.displays.DisplayNotifierProvider;
import io.intino.konos.server.activity.spark.ActivitySparkManager;
import io.intino.konos.server.activity.spark.resources.DisplayRequester;

public class CatalogViewListDisplayRequester extends DisplayRequester {

	public CatalogViewListDisplayRequester(ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws KonosException {
		CatalogViewListDisplay display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("selectView")) display.selectView(manager.fromQuery("value", String.class));
	}
}