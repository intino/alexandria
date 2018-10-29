package io.intino.alexandria.ui.displays;

import io.intino.alexandria.ui.model.View;
import io.intino.alexandria.ui.model.mold.Stamp;
import io.intino.konos.framework.Box;
import io.intino.konos.alexandria.ui.displays.notifiers.AlexandriaViewContainerMoldNotifier;
import io.intino.konos.alexandria.ui.schemas.Item;

import static io.intino.alexandria.ui.helpers.ElementHelper.itemDisplayProvider;


public class AlexandriaViewContainerMold extends AlexandriaViewContainer<AlexandriaViewContainerMoldNotifier> {

	public AlexandriaViewContainerMold(Box box) {
		super(box);
	}

	@Override
	protected void init() {
		super.init();
		createItemDisplay();
	}

	@Override
	public void refresh() {
		super.refresh();
		child(AlexandriaItem.class).refresh();
	}

	@Override
	public void refreshValidation(String validationMessage, Stamp stamp, Item item) {
		child(AlexandriaItem.class).refreshValidation(validationMessage, stamp, item);
	}

	private void createItemDisplay() {
		View view = view();
		AlexandriaItem display = new AlexandriaItem(box);
		display.route(routeSubPath());
		display.mold(view.mold());
		display.context(context());
		display.item(target());
		display.mode("custom");
		display.provider(itemDisplayProvider(provider(), view));
		display.onOpenItem(this::notifyOpenItem);
		display.onOpenItemDialog(this::openItemDialogOperation);
		display.onOpenItemCatalog(this::openItemCatalogOperation);
		display.onExecuteItemTask(this::executeItemTaskOperation);
		add(display);
		display.personifyOnce();
	}

}