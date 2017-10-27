package io.intino.konos.server.activity.displays.catalogs;

import io.intino.konos.exceptions.KonosException;
import io.intino.konos.server.activity.displays.DisplayNotifierProvider;
import io.intino.konos.server.activity.displays.schemas.GroupingGroup;
import io.intino.konos.server.activity.displays.schemas.GroupingSelection;
import io.intino.konos.server.activity.spark.ActivitySparkManager;
import io.intino.konos.server.activity.spark.resources.DisplayRequester;

public class CatalogDisplayRequester extends DisplayRequester {

	public CatalogDisplayRequester(ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws KonosException {
		CatalogDisplay display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("selectGrouping")) display.selectGrouping(manager.fromQuery("value", GroupingSelection.class));
		else if (operation.equals("deleteGroupingGroup")) display.deleteGroupingGroup(manager.fromQuery("value", GroupingGroup.class));
		else if (operation.equals("clearFilter")) display.clearFilter();
		else if (operation.equals("navigate")) display.navigate(manager.fromQuery("value", String.class));
	}
}