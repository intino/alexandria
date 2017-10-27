package io.intino.konos.server.activity.displays.panels.views;

import io.intino.konos.Box;
import io.intino.konos.server.activity.displays.elements.ElementView;
import io.intino.konos.server.activity.displays.elements.items.ItemDisplay;

import static io.intino.konos.server.activity.helpers.ElementHelper.itemDisplayProvider;

public class PanelCustomViewDisplay extends PanelViewDisplay<PanelCustomViewDisplayNotifier> {

	public PanelCustomViewDisplay(Box box) {
		super(box);
	}

	@Override
	protected void init() {
		super.init();
		createRecordDisplay();
	}

	@Override
	public void refresh() {
		super.refresh();
		child(ItemDisplay.class).refresh();
	}

	private void createRecordDisplay() {
		ElementView view = view();
		ItemDisplay display = new ItemDisplay(box);
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