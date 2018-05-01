package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.displays.notifiers.AlexandriaContainerViewDisplayNotifier;
import io.intino.konos.alexandria.ui.model.Item;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;


public class AlexandriaContainerViewDisplay extends AlexandriaContainerView<AlexandriaContainerViewDisplayNotifier> {

    public AlexandriaContainerViewDisplay(Box box) {
        super(box);
    }

    @Override
    protected void init() {
        super.init();
        PanelView rawView = (PanelView) definition().raw();
        RenderDisplay render = rawView.render();
        AlexandriaDisplay display = render.display(target(), loadingListener(), instantListener());
        if (display == null) return;
        sendDisplayType(display);
        add(display);
        display.personifyOnce(id());
    }

    @Override
    public void refresh() {
        super.refresh();
        Optional.ofNullable(child(AlexandriaDisplay.class)).ifPresent(AlexandriaDisplay::refresh);
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