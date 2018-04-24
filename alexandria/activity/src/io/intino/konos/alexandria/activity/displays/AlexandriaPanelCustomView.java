package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaPanelCustomViewNotifier;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.schemas.Item;

import static io.intino.konos.alexandria.activity.helpers.ElementHelper.itemDisplayProvider;

public class AlexandriaPanelCustomView extends AlexandriaPanelView<AlexandriaPanelCustomViewNotifier> {

	public AlexandriaPanelCustomView(Box box) {
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
		AlexandriaElementViewDefinition view = definition();
		AlexandriaItem display = new AlexandriaItem(box);
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