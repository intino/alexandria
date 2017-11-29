package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.activity.spark.ActivityFile;
import io.intino.konos.alexandria.exceptions.AlexandriaException;
import io.intino.konos.alexandria.activity.schemas.DialogInput;
import io.intino.konos.alexandria.activity.schemas.DialogInputResource;
import io.intino.konos.alexandria.activity.spark.ActivitySparkManager;
import io.intino.konos.alexandria.activity.spark.resources.AlexandriaDisplayRequester;

public class AlexandriaDialogDisplayRequester extends AlexandriaDisplayRequester {

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
			ActivityFile file = display.downloadResource(manager.fromQuery("value", String.class));
			manager.write(file.content(), file.label());
		}
	}
}