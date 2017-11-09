package io.intino.konos.alexandria.activity.displays.requesters;

import io.intino.konos.alexandria.activity.displays.AlexandriaMenuLayoutDisplay;

import io.intino.konos.alexandria.exceptions.AlexandriaException;
import io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifierProvider;
import io.intino.konos.alexandria.activity.spark.ActivitySparkManager;
import io.intino.konos.alexandria.activity.spark.resources.DisplayRequester;

public class AlexandriaMenuLayoutDisplayRequester extends DisplayRequester {

	public AlexandriaMenuLayoutDisplayRequester(ActivitySparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws AlexandriaException {
		AlexandriaMenuLayoutDisplay display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("logout")) display.logout();
		else if (operation.equals("selectItem")) display.selectItem(manager.fromQuery("value", String.class));
	}
}