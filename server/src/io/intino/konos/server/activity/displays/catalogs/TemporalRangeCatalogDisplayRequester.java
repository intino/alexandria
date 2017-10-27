package io.intino.konos.server.activity.displays.catalogs;

import io.intino.konos.server.activity.displays.DisplayNotifierProvider;
import io.intino.konos.server.activity.spark.ActivitySparkManager;

public class TemporalRangeCatalogDisplayRequester extends TemporalTimeCatalogDisplayRequester {

	public TemporalRangeCatalogDisplayRequester(ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

}