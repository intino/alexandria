package io.intino.konos.server.activity.displays.elements.items;

import io.intino.konos.exceptions.KonosException;
import io.intino.konos.server.activity.displays.DisplayNotifierProvider;
import io.intino.konos.server.activity.spark.ActivitySparkManager;
import io.intino.konos.server.activity.spark.resources.DisplayRequester;

public class DialogContainerDisplayRequester extends DisplayRequester {

	public DialogContainerDisplayRequester(ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws KonosException {
		DialogContainerDisplay display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("dialogAssertionMade")) display.dialogAssertionMade(manager.fromQuery("value", String.class));
	}
}