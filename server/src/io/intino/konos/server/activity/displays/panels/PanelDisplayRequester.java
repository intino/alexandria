package io.intino.konos.server.activity.displays.panels;

import io.intino.konos.exceptions.KonosException;
import io.intino.konos.server.activity.displays.DisplayNotifierProvider;
import io.intino.konos.server.activity.spark.ActivitySparkManager;
import io.intino.konos.server.activity.spark.resources.DisplayRequester;

public class PanelDisplayRequester extends DisplayRequester {

	public PanelDisplayRequester(ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws KonosException {
		PanelDisplay display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("selectView")) display.selectView(manager.fromQuery("value", String.class));
		else if (operation.equals("navigate")) display.navigate(manager.fromQuery("value", String.class));
	}
}