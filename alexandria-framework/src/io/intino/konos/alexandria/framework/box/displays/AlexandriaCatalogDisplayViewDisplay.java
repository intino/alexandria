package io.intino.konos.alexandria.framework.box.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.foundation.activity.displays.ActivityDisplay;
import io.intino.konos.alexandria.foundation.activity.displays.Display;
import io.intino.konos.alexandria.framework.box.displays.notifiers.AlexandriaCatalogDisplayViewDisplayNotifier;
import io.intino.konos.alexandria.framework.box.displays.providers.CatalogViewDisplayProvider;
import io.intino.konos.alexandria.framework.box.model.Catalog;
import io.intino.konos.alexandria.framework.box.model.catalog.views.DisplayView;
import io.intino.konos.alexandria.framework.box.schemas.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AlexandriaCatalogDisplayViewDisplay extends ActivityDisplay<AlexandriaCatalogDisplayViewDisplayNotifier> implements AlexandriaCatalogViewDisplay {
    private ElementView<Catalog> view;
    private CatalogViewDisplayProvider provider;
    private List<Consumer<Boolean>> loadingListeners = new ArrayList<>();
    private Display display;

    public AlexandriaCatalogDisplayViewDisplay(Box box) {
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
        notifier.displayType(display.getClass().getSimpleName());
    }

    private Consumer<Boolean> loadingListener() {
        return value -> AlexandriaCatalogDisplayViewDisplay.this.notifyLoading((Boolean) value);
    }

    private Consumer<CatalogInstantBlock> instantListener() {
        return block -> AlexandriaCatalogDisplayViewDisplay.this.selectInstant((CatalogInstantBlock) block);
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