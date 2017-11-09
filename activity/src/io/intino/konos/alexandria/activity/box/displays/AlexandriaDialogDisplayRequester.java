package io.intino.konos.alexandria.activity.box.displays;

import io.intino.konos.alexandria.exceptions.AlexandriaException;
import io.intino.konos.alexandria.activity.box.displays.AlexandriaDisplayNotifierProvider;
import io.intino.konos.alexandria.activity.box.schemas.DialogInput;
import io.intino.konos.alexandria.activity.box.schemas.DialogInputResource;
import io.intino.konos.alexandria.activity.box.spark.ActivitySparkManager;
import io.intino.konos.alexandria.activity.box.spark.resources.DisplayRequester;

public class AlexandriaDialogDisplayRequester extends DisplayRequester {

	public AlexandriaDialogDisplayRequester(ActivitySparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws AlexandriaException {
		AlexandriaDialogDisplay display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("saveValue")) display.saveValue(manager.fromQuery("value", DialogInput.class));
		else if (operation.equals("addValue")) display.addValue(manager.fromQuery("value", String.class));
		else if (operation.equals("removeValue")) display.removeValue(manager.fromQuery("value", String.class));
		else if (operation.equals("execute")) display.execute(manager.fromQuery("value", String.class));
		else if (operation.equals("uploadResource")) display.uploadResource(manager.fromQuery("value", DialogInputResource.class));
		else if (operation.equals("downloadResource")) {
			io.intino.konos.alexandria.activity.box.spark.ActivityFile file = display.downloadResource(manager.fromQuery("value", String.class));
			manager.write(file.content(), file.label());
		}
	}
}