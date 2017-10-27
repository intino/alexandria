package io.intino.konos.server.activity.displays.catalogs.views;

import io.intino.konos.Box;
import io.intino.konos.server.activity.displays.ActivityDisplay;
import io.intino.konos.server.activity.displays.Display;
import io.intino.konos.server.activity.displays.catalogs.CatalogInstantBlock;
import io.intino.konos.server.activity.displays.catalogs.model.Catalog;
import io.intino.konos.server.activity.displays.catalogs.model.views.DisplayView;
import io.intino.konos.server.activity.displays.catalogs.providers.CatalogViewDisplayProvider;
import io.intino.konos.server.activity.displays.elements.ElementView;
import io.intino.konos.server.activity.displays.schemas.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CatalogDisplayViewDisplay extends ActivityDisplay<CatalogDisplayViewDisplayNotifier> implements CatalogViewDisplay {
    private ElementView<Catalog> view;
    private CatalogViewDisplayProvider provider;
    private List<Consumer<Boolean>> loadingListeners = new ArrayList<>();
    private Display display;

    public CatalogDisplayViewDisplay(Box box) {
        super(box);
    }

    @Override
    public void view(ElementView view) {
        this.view = view;
    }

    @Override
    public void provider(CatalogViewDisplayProvider provider) {
        this.provider = provider;
    }

    @Override
    public void onOpenItemDialog(Consumer<OpenItemDialogEvent> location) {
    }

    @Override
    public void onExecuteItemTask(Consumer<ExecuteItemTaskEvent> params) {
    }

    @Override
    public void onOpenItem(Consumer<OpenItemEvent> listener) {
    }

    @Override
    public void reset() {
    }

    @Override
    public ElementView view() {
        return view;
    }

    @Override
    public void onLoading(Consumer<Boolean> listener) {
        loadingListeners.add(listener);
    }

    @Override
    public void refresh() {
        super.refresh();
        this.display.refresh();
    }

    @Override
    protected void init() {
        super.init();
        this.display = ((DisplayView)view().raw()).display(provider.concept(), loadingListener(), instantListener());
        sendDisplayType(display);
        add(display);
        display.personifyOnce();
    }

    private void sendDisplayType(Display display) {
        // TODO Mario -> Incluirlo en el html
        notifier.displayType(display.getClass().getSimpleName());
    }

    private Consumer<Boolean> loadingListener() {
        return value -> CatalogDisplayViewDisplay.this.notifyLoading((Boolean) value);
    }

    private Consumer<CatalogInstantBlock> instantListener() {
        return block -> CatalogDisplayViewDisplay.this.selectInstant((CatalogInstantBlock) block);
    }

    private void selectInstant(CatalogInstantBlock block) {
        provider.selectInstant(block);
    }

    private void notifyLoading(Boolean loading) {
        loadingListeners.forEach(l -> l.accept(loading));
    }

    @Override
    public void refresh(Item... items) {
    }
}