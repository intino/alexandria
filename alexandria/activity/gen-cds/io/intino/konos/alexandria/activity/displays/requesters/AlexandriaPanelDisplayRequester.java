package io.intino.konos.alexandria.activity.displays.requesters;

import io.intino.konos.alexandria.activity.displays.AlexandriaPanelDisplay;

import io.intino.konos.alexandria.exceptions.AlexandriaException;
import io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifierProvider;
import io.intino.konos.alexandria.activity.spark.ActivitySparkManager;
import io.intino.konos.alexandria.activity.spark.resources.DisplayRequester;

public class AlexandriaPanelDisplayRequester extends DisplayRequester {

	public AlexandriaPanelDisplayRequester(ActivitySparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws AlexandriaException {
		AlexandriaPanelDisplay display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("selectView")) display.selectView(manager.fromQuery("value", String.class));
		else if (operation.equals("navigate")) display.navigate(manager.fromQuery("value", String.class));
	}
}