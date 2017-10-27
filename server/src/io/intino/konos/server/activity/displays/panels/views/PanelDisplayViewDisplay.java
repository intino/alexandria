package io.intino.konos.server.activity.displays.panels.views;

import io.intino.konos.Box;
import io.intino.konos.server.activity.displays.Display;
import io.intino.konos.server.activity.displays.catalogs.CatalogDisplay;
import io.intino.konos.server.activity.displays.catalogs.CatalogInstantBlock;
import io.intino.konos.server.activity.displays.elements.model.Item;
import io.intino.konos.server.activity.displays.elements.model.renders.RenderDisplay;
import io.intino.konos.server.activity.displays.panels.model.View;

import java.util.List;
import java.util.function.Consumer;

public class PanelDisplayViewDisplay extends PanelViewDisplay<PanelDisplayViewDisplayNotifier> {

    public PanelDisplayViewDisplay(Box box) {
        super(box);
    }

    @Override
    protected void init() {
        super.init();
        View rawView = view().raw();
        RenderDisplay render = rawView.render();
        Display display = render.display(loadingListener(), instantListener());
        add(display);
        display.personifyOnce(id());
    }

    private Consumer<Boolean> loadingListener() {
        return value -> PanelDisplayViewDisplay.this.notifyLoading((Boolean) value);
    }

    private Consumer<CatalogInstantBlock> instantListener() {
        return block -> PanelDisplayViewDisplay.this.selectInstant((CatalogInstantBlock) block);
    }

    private void selectInstant(CatalogInstantBlock block) {
        CatalogDisplay display = provider().openElement(block.catalog());
        List<String> entities = block.entities();
        display.filterAndNotify(item -> entities.contains(((Item)item).id()));
        display.refreshView();
    }

}