package io.intino.konos.alexandria.activity.displays.requesters;

import io.intino.konos.alexandria.activity.displays.AlexandriaCatalogListViewDisplay;
import io.intino.konos.alexandria.activity.schemas.*;

import io.intino.konos.alexandria.exceptions.AlexandriaException;
import io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifierProvider;
import io.intino.konos.alexandria.activity.spark.ActivitySparkManager;
import io.intino.konos.alexandria.activity.spark.resources.DisplayRequester;

public class AlexandriaCatalogListViewDisplayRequester extends DisplayRequester {

	public AlexandriaCatalogListViewDisplayRequester(ActivitySparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws AlexandriaException {
		AlexandriaCatalogListViewDisplay display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("openItem")) display.openItem(manager.fromQuery("value", String.class));
		else if (operation.equals("selectItems")) display.selectItems(manager.fromQuery("value", String[].class));
		else if (operation.equals("renderExpandedPictures")) display.renderExpandedPictures();
		else if (operation.equals("itemRefreshed")) display.itemRefreshed(manager.fromQuery("value", String.class));
		else if (operation.equals("selectSorting")) display.selectSorting(manager.fromQuery("value", Sorting.class));
		else if (operation.equals("page")) display.page(manager.fromQuery("value", Integer.class));
		else if (operation.equals("filter")) display.filter(manager.fromQuery("value", String.class));
		else if (operation.equals("createClusterGroup")) display.createClusterGroup(manager.fromQuery("value", ClusterGroup.class));
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
		else if (operation.equals("saveItem")) display.saveItem(manager.fromQuery("value", SaveItemParameters.class));
	}
}