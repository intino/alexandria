package io.intino.konos.server.activity.displays.catalogs;

import io.intino.konos.exceptions.KonosException;
import io.intino.konos.server.activity.displays.DisplayNotifierProvider;
import io.intino.konos.server.activity.displays.schemas.GroupingSelection;
import io.intino.konos.server.activity.spark.ActivitySparkManager;

public class TemporalTimeCatalogDisplayRequester extends CatalogDisplayRequester {

	public TemporalTimeCatalogDisplayRequester(ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws KonosException {
		TemporalCatalogDisplay display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("selectGrouping")) display.selectGrouping(manager.fromQuery("value", GroupingSelection.class));
		else if (operation.equals("clearFilter")) display.clearFilter();
		else if (operation.equals("timezoneOffset")) display.timezoneOffset(manager.fromQuery("value", Integer.class));
		else if (operation.equals("navigate")) display.navigate(manager.fromQuery("value", String.class));
	}
}