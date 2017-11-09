package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaPanelDisplayViewDisplayNotifier;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.panel.View;
import io.intino.konos.alexandria.activity.model.renders.RenderDisplay;

import java.util.List;
import java.util.function.Consumer;

public class AlexandriaPanelDisplayViewDisplay extends AlexandriaPanelViewDisplay<AlexandriaPanelDisplayViewDisplayNotifier> {

    public AlexandriaPanelDisplayViewDisplay(Box box) {
        super(box);
    }

    @Override
    protected void init() {
        super.init();
        View rawView = view().raw();
        RenderDisplay render = rawView.render();
        AlexandriaDisplay display = render.display(loadingListener(), instantListener());
        sendDisplayType(display);
        add(display);
        display.personifyOnce(id());
    }

    private void sendDisplayType(AlexandriaDisplay display) {
        notifier.displayType(display.name());
    }

    private Consumer<Boolean> loadingListener() {
        return value -> AlexandriaPanelDisplayViewDisplay.this.notifyLoading((Boolean) value);
    }

    private Consumer<CatalogInstantBlock> instantListener() {
        return block -> AlexandriaPanelDisplayViewDisplay.this.selectInstant((CatalogInstantBlock) block);
    }

    private void selectInstant(CatalogInstantBlock block) {
        AlexandriaCatalogDisplay display = provider().openElement(block.catalog());
        List<String> items = block.items();
        display.filterAndNotify(item -> items.contains(((Item)item).id()));
        display.refreshView();
    }

}