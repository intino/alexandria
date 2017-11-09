package io.intino.konos.alexandria.activity.displays.requesters;

import io.intino.konos.alexandria.activity.displays.AlexandriaDialogContainerDisplay;

import io.intino.konos.alexandria.exceptions.AlexandriaException;
import io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifierProvider;
import io.intino.konos.alexandria.activity.spark.ActivitySparkManager;
import io.intino.konos.alexandria.activity.spark.resources.DisplayRequester;

public class AlexandriaDialogContainerDisplayRequester extends DisplayRequester {

	public AlexandriaDialogContainerDisplayRequester(ActivitySparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws AlexandriaException {
		AlexandriaDialogContainerDisplay display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("dialogAssertionMade")) display.dialogAssertionMade(manager.fromQuery("value", String.class));
	}
}