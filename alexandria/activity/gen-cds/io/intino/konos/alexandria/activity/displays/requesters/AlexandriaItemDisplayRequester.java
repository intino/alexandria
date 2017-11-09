package io.intino.konos.alexandria.activity.displays.requesters;

import io.intino.konos.alexandria.activity.displays.AlexandriaItemDisplay;
import io.intino.konos.alexandria.activity.schemas.*;

import io.intino.konos.alexandria.exceptions.AlexandriaException;
import io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifierProvider;
import io.intino.konos.alexandria.activity.spark.ActivitySparkManager;
import io.intino.konos.alexandria.activity.spark.resources.DisplayRequester;

public class AlexandriaItemDisplayRequester extends DisplayRequester {

	public AlexandriaItemDisplayRequester(ActivitySparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws AlexandriaException {
		AlexandriaItemDisplay display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("itemStampsReady")) display.itemStampsReady(manager.fromQuery("value", String.class));
		else if (operation.equals("selectItem")) display.selectItem(manager.fromQuery("value", Reference.class));
		else if (operation.equals("selectElement")) display.selectElement(manager.fromQuery("value", Reference.class));
		else if (operation.equals("executeOperation")) display.executeOperation(manager.fromQuery("value", ElementOperationParameters.class));
		else if (operation.equals("downloadOperation")) {
			io.intino.konos.alexandria.activity.spark.ActivityFile file = display.downloadOperation(manager.fromQuery("value", ElementOperationParameters.class));
			manager.write(file.content(), file.label());
		}
		else if (operation.equals("openItemDialogOperation")) display.openItemDialogOperation(manager.fromQuery("value", OpenItemDialogParameters.class));
		else if (operation.equals("executeItemTaskOperation")) display.executeItemTaskOperation(manager.fromQuery("value", ExecuteItemTaskParameters.class));
		else if (operation.equals("downloadItemOperation")) {
			io.intino.konos.alexandria.activity.spark.ActivityFile file = display.downloadItemOperation(manager.fromQuery("value", DownloadItemParameters.class));
			manager.write(file.content(), file.label());
		}
		else if (operation.equals("exportItemOperation")) {
			io.intino.konos.alexandria.activity.spark.ActivityFile file = display.exportItemOperation(manager.fromQuery("value", ExportItemParameters.class));
			manager.write(file.content(), file.label());
		}
		else if (operation.equals("saveItem")) display.saveItem(manager.fromQuery("value", SaveItemParameters.class));
	}
}