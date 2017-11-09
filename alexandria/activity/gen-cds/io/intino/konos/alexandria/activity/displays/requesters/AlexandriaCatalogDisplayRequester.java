package io.intino.konos.alexandria.activity.displays.requesters;

import io.intino.konos.alexandria.activity.displays.AlexandriaCatalogDisplay;
import io.intino.konos.alexandria.activity.schemas.*;

import io.intino.konos.alexandria.exceptions.AlexandriaException;
import io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifierProvider;
import io.intino.konos.alexandria.activity.spark.ActivitySparkManager;
import io.intino.konos.alexandria.activity.spark.resources.DisplayRequester;

public class AlexandriaCatalogDisplayRequester extends DisplayRequester {

	public AlexandriaCatalogDisplayRequester(ActivitySparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws AlexandriaException {
		AlexandriaCatalogDisplay display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("selectGrouping")) display.selectGrouping(manager.fromQuery("value", GroupingSelection.class));
		else if (operation.equals("deleteGroupingGroup")) display.deleteGroupingGroup(manager.fromQuery("value", GroupingGroup.class));
		else if (operation.equals("clearFilter")) display.clearFilter();
		else if (operation.equals("navigate")) display.navigate(manager.fromQuery("value", String.class));
	}
}