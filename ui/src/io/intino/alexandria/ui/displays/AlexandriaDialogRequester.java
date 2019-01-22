package io.intino.alexandria.ui.displays;

import io.intino.alexandria.ui.spark.UIFile;
import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.ui.schemas.DialogInput;
import io.intino.alexandria.ui.schemas.DialogInputResource;
import io.intino.alexandria.ui.spark.UISparkManager;
import io.intino.alexandria.ui.displays.requesters.AlexandriaDisplayRequester;

public class AlexandriaDialogRequester extends AlexandriaDisplayRequester {

	public AlexandriaDialogRequester(UISparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws AlexandriaException {
		AlexandriaDialog display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("saveValue")) display.saveValue(manager.fromQuery("value", DialogInput.class));
		else if (operation.equals("addValue")) display.addValue(manager.fromQuery("value", String.class));
		else if (operation.equals("removeValue")) display.removeValue(manager.fromQuery("value", String.class));
		else if (operation.equals("execute")) display.execute(manager.fromQuery("value", String.class));
		else if (operation.equals("uploadResource")) display.uploadResource(manager.fromQuery("value", DialogInputResource.class));
		else if (operation.equals("downloadResource")) {
			UIFile file = display.downloadResource(manager.fromQuery("value", String.class));
			manager.write(file.content(), file.label());
		}
	}
}