package io.intino.konos.server.activity.displays.layouts;

import io.intino.konos.exceptions.KonosException;
import io.intino.konos.server.activity.displays.DisplayNotifierProvider;
import io.intino.konos.server.activity.spark.ActivitySparkManager;
import io.intino.konos.server.activity.spark.resources.DisplayRequester;

public class LayoutDisplayRequester extends DisplayRequester {

	public LayoutDisplayRequester(ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws KonosException {
		LayoutDisplay display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("logout")) display.logout();
		else if (operation.equals("selectItem")) display.selectItem(manager.fromQuery("value", String.class));
	}
}