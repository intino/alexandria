package io.intino.konos.alexandria.activity.box.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.box.displays.notifiers.AlexandriaPanelCustomViewDisplayNotifier;

import static io.intino.konos.alexandria.activity.box.helpers.ElementHelper.itemDisplayProvider;

public class AlexandriaPanelCustomViewDisplay extends AlexandriaPanelViewDisplay<AlexandriaPanelCustomViewDisplayNotifier> {

	public AlexandriaPanelCustomViewDisplay(Box box) {
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
		child(AlexandriaItemDisplay.class).refresh();
	}

	private void createItemDisplay() {
		ElementView view = view();
		AlexandriaItemDisplay display = new AlexandriaItemDisplay(box);
		display.mold(view.mold());
		display.context(context());
		display.item(target());
		display.mode("custom");
		display.provider(itemDisplayProvider(provider(), view));
		display.onOpenItem(this::notifyOpenItem);
		display.onOpenItemDialog(this::notifyOpenItemDialog);
		display.onExecuteItemTask(this::notifyExecuteItemTaskOperation);
		add(display);
		display.personifyOnce();
	}

}