package io.intino.konos.alexandria.activity.displays.requesters;

import io.intino.konos.alexandria.activity.displays.AlexandriaTemporalTimeCatalogDisplay;
import io.intino.konos.alexandria.activity.schemas.*;

import io.intino.konos.alexandria.exceptions.AlexandriaException;
import io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifierProvider;
import io.intino.konos.alexandria.activity.spark.ActivitySparkManager;
import io.intino.konos.alexandria.activity.spark.resources.DisplayRequester;

public class AlexandriaTemporalTimeCatalogDisplayRequester extends DisplayRequester {

	public AlexandriaTemporalTimeCatalogDisplayRequester(ActivitySparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws AlexandriaException {
		AlexandriaTemporalTimeCatalogDisplay display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("selectGrouping")) display.selectGrouping(manager.fromQuery("value", GroupingSelection.class));
		else if (operation.equals("clearFilter")) display.clearFilter();
		else if (operation.equals("timezoneOffset")) display.timezoneOffset(manager.fromQuery("value", Integer.class));
		else if (operation.equals("navigate")) display.navigate(manager.fromQuery("value", String.class));
	}
}