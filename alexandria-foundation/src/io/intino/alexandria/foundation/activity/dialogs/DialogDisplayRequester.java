package io.intino.alexandria.foundation.activity.dialogs;

import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.foundation.activity.displays.DisplayNotifierProvider;
import io.intino.alexandria.foundation.activity.schemas.DialogInput;
import io.intino.alexandria.foundation.activity.schemas.DialogInputResource;
import io.intino.alexandria.foundation.activity.spark.ActivitySparkManager;
import io.intino.alexandria.foundation.activity.spark.resources.DisplayRequester;

public class DialogDisplayRequester extends DisplayRequester {

	public DialogDisplayRequester(ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws AlexandriaException {
		DialogDisplay display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("saveValue")) display.saveValue(manager.fromQuery("value", DialogInput.class));
		else if (operation.equals("addValue")) display.addValue(manager.fromQuery("value", String.class));
		else if (operation.equals("removeValue")) display.removeValue(manager.fromQuery("value", String.class));
		else if (operation.equals("execute")) display.execute(manager.fromQuery("value", String.class));
		else if (operation.equals("uploadResource")) display.uploadResource(manager.fromQuery("value", DialogInputResource.class));
		else if (operation.equals("downloadResource")) {
			io.intino.alexandria.foundation.activity.spark.ActivityFile file = display.downloadResource(manager.fromQuery("value", String.class));
			manager.write(file.content(), file.label());
		}
	}
}