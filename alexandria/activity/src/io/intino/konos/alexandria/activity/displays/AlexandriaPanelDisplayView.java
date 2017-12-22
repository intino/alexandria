package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaPanelDisplayViewNotifier;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.panel.View;
import io.intino.konos.alexandria.activity.model.renders.RenderDisplay;

import java.util.List;
import java.util.function.Consumer;

public class AlexandriaPanelDisplayView extends AlexandriaPanelView<AlexandriaPanelDisplayViewNotifier> {

    public AlexandriaPanelDisplayView(Box box) {
        super(box);
    }

    @Override
    protected void init() {
        super.init();
        View rawView = view().raw();
        RenderDisplay render = rawView.render();
        AlexandriaDisplay display = render.display(loadingListener(), instantListener());
        if (display == null) return;
        sendDisplayType(display);
        add(display);
        display.personifyOnce(id());
    }

    private void sendDisplayType(AlexandriaDisplay display) {
        notifier.displayType(display.name());
    }

    private Consumer<Boolean> loadingListener() {
        return value -> AlexandriaPanelDisplayView.this.notifyLoading((Boolean) value);
    }

    private Consumer<CatalogInstantBlock> instantListener() {
        return block -> AlexandriaPanelDisplayView.this.selectInstant((CatalogInstantBlock) block);
    }

    private void selectInstant(CatalogInstantBlock block) {
        AlexandriaCatalog display = provider().openElement(block.catalog());
        List<String> items = block.items();
        display.filterAndNotify(item -> items.contains(((Item)item).id()));
        display.refreshView();
    }

}