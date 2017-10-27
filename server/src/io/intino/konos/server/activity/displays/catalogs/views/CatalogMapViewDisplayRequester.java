package io.intino.konos.server.activity.displays.catalogs.views;

import io.intino.konos.exceptions.KonosException;
import io.intino.konos.server.activity.displays.DisplayNotifierProvider;
import io.intino.konos.server.activity.displays.schemas.*;
import io.intino.konos.server.activity.spark.ActivitySparkManager;
import io.intino.konos.server.activity.spark.resources.DisplayRequester;

public class CatalogMapViewDisplayRequester extends DisplayRequester {

	public CatalogMapViewDisplayRequester(ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws KonosException {
		CatalogMapViewDisplay display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("location")) display.location(manager.fromQuery("value", Bounds.class));
		else if (operation.equals("page")) display.page(manager.fromQuery("value", Integer.class));
		else if (operation.equals("executeOperation")) display.executeOperation(manager.fromQuery("value", ElementOperationParameters.class));
		else if (operation.equals("downloadOperation")) {
			io.intino.konos.server.activity.spark.ActivityFile file = display.downloadOperation(manager.fromQuery("value", ElementOperationParameters.class));
			manager.write(file.content(), file.label());
		}
		else if (operation.equals("openItemDialogOperation")) display.openItemDialogOperation(manager.fromQuery("value", OpenItemDialogParameters.class));
		else if (operation.equals("executeItemTaskOperation")) display.executeItemTaskOperation(manager.fromQuery("value", ExecuteItemTaskParameters.class));
		else if (operation.equals("downloadItemOperation")) {
			io.intino.konos.server.activity.spark.ActivityFile file = display.downloadItemOperation(manager.fromQuery("value", DownloadItemParameters.class));
			manager.write(file.content(), file.label());
		}
	}
}