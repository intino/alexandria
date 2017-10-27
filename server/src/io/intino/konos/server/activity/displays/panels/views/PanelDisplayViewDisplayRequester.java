package io.intino.konos.server.activity.displays.panels.views;

import io.intino.konos.exceptions.KonosException;
import io.intino.konos.server.activity.displays.DisplayNotifierProvider;
import io.intino.konos.server.activity.spark.ActivitySparkManager;
import io.intino.konos.server.activity.spark.resources.DisplayRequester;

public class PanelDisplayViewDisplayRequester extends DisplayRequester {

	public PanelDisplayViewDisplayRequester(ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws KonosException {
		PanelDisplayViewDisplay display = display();
		if (display == null) return;
		String operation = operation();


	}
}