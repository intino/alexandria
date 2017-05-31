package io.intino.konos.server.activity.dialogs;

import io.intino.konos.exceptions.KonosException;
import io.intino.konos.server.activity.dialogs.schemas.DialogInput;
import io.intino.konos.server.activity.displays.DisplayNotifierProvider;
import io.intino.konos.server.activity.spark.ActivitySparkManager;
import io.intino.konos.server.activity.spark.resources.DisplayRequester;

public class DialogRequester extends DisplayRequester {

	public DialogRequester(ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws KonosException {
		DialogDisplay display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("update")) display.update(manager.fromQuery("value", DialogInput.class));
		else if (operation.equals("execute")) display.execute();
	}
}