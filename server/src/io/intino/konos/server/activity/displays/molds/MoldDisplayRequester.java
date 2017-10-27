package io.intino.konos.server.activity.displays.molds;

import io.intino.konos.exceptions.KonosException;
import io.intino.konos.server.activity.displays.DisplayNotifierProvider;
import io.intino.konos.server.activity.displays.catalogs.CatalogDisplay;
import io.intino.konos.server.activity.spark.ActivitySparkManager;
import io.intino.konos.server.activity.spark.resources.DisplayRequester;

public class MoldDisplayRequester extends DisplayRequester {

	public MoldDisplayRequester(ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws KonosException {
		MoldDisplay display = display();
		if (display == null) return;
		String operation = operation();
	}
}