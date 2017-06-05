package io.intino.konos.server.activity.dialogs;

import io.intino.konos.exceptions.KonosException;
import io.intino.konos.server.activity.dialogs.schemas.DialogInput;
import io.intino.konos.server.activity.dialogs.schemas.DialogInputResource;
import io.intino.konos.server.activity.dialogs.schemas.DialogInputValueIdentifier;
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

		if (operation.equals("addValue")) display.addValue(manager.fromQuery("value", DialogInput.class));
		else if (operation.equals("removeValue")) display.removeValue(manager.fromQuery("value", DialogInputValueIdentifier.class));
		else if (operation.equals("execute")) display.execute();
		else if (operation.equals("uploadResource")) display.uploadResource(manager.fromQuery("value", DialogInputResource.class));
		else if (operation.equals("downloadResource")) {
			io.intino.konos.server.activity.spark.ActivityFile file = display.downloadResource(manager.fromQuery("value", String.class));
			manager.write(file.content(), file.label());
		}
	}
}