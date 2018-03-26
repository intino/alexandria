package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaPanelCustomViewNotifier;

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

	private void createItemDisplay() {
		ElementView view = view();
		AlexandriaItem display = new AlexandriaItem(box);
		display.mold(view.mold());
		display.context(context());
		display.item(target());
		display.mode("custom");
		display.provider(itemDisplayProvider(provider(), view));
		display.onOpenItem(this::notifyOpenItem);
		display.onOpenItemDialog(this::notifyOpenItemDialog);
		display.onOpenItemCatalog(this::notifyOpenItemCatalog);
		display.onExecuteItemTask(this::notifyExecuteItemTaskOperation);
		add(display);
		display.personifyOnce();
	}

}