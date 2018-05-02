package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.displays.notifiers.AlexandriaContainerViewMoldNotifier;
import io.intino.konos.alexandria.ui.model.View;
import io.intino.konos.alexandria.ui.model.mold.Stamp;
import io.intino.konos.alexandria.ui.schemas.Item;

import static io.intino.konos.alexandria.ui.helpers.ElementHelper.itemDisplayProvider;


public class AlexandriaContainerViewMold extends AlexandriaContainerView<AlexandriaContainerViewMoldNotifier> {

    public AlexandriaContainerViewMold(Box box) {
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