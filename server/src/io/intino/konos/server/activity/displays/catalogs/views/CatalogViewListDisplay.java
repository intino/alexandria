package io.intino.konos.server.activity.displays.catalogs.views;

import io.intino.konos.Box;
import io.intino.konos.server.activity.displays.ActivityDisplay;
import io.intino.konos.server.activity.displays.Display;
import io.intino.konos.server.activity.displays.catalogs.model.views.*;
import io.intino.konos.server.activity.displays.catalogs.providers.CatalogViewDisplayProvider;
import io.intino.konos.server.activity.displays.elements.ElementView;
import io.intino.konos.server.activity.displays.elements.ElementViewDisplay;
import io.intino.konos.server.activity.displays.elements.builders.ReferenceBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.intino.konos.server.activity.displays.elements.ElementViewDisplay.*;

public class CatalogViewListDisplay extends ActivityDisplay<CatalogViewListDisplayNotifier> {
    private List<Consumer<CatalogViewDisplay>> selectListeners = new ArrayList<>();
    private Map<String, Function<ElementView, ? extends Display>> builders = new HashMap<>();
    private List<ElementView> viewList;
    private CatalogViewDisplayProvider provider;
    private List<Consumer<Boolean>> loadingListeners = new ArrayList<>();
    private List<Consumer<OpenItemEvent>> openItemListeners = new ArrayList<>();
    private List<Consumer<OpenItemDialogEvent>> openItemDialogListeners = new ArrayList<>();
    private List<Consumer<ExecuteItemTaskEvent>> executeItemTaskListeners = new ArrayList<>();
    private Map<String, CatalogViewDisplay> viewDisplayMap = new HashMap<>();

    public CatalogViewListDisplay(Box box) {
        super(box);
        registerBuilders();
    }

    public void itemProvider(CatalogViewDisplayProvider provider) {
        this.provider = provider;
    }

    public void viewList(List<ElementView> viewList) {
        this.viewList = viewList;
    }

    public void onSelectView(Consumer<CatalogViewDisplay> listener) {
        selectListeners.add(listener);
    }

    public void onLoading(Consumer<Boolean> listener) {
        loadingListeners.add(listener);
    }

    public void onOpenItem(Consumer<OpenItemEvent> listener) {
        openItemListeners.add(listener);
    }

    public void onOpenItemDialog(Consumer<OpenItemDialogEvent> listener) {
        openItemDialogListeners.add(listener);
    }

    public void onExecuteItemTask(Consumer<ExecuteItemTaskEvent> listener) {
        executeItemTaskListeners.add(listener);
    }

    public List<CatalogViewDisplay> viewList() {
        return new ArrayList<>(viewDisplayMap.values());
    }

    private void registerBuilders() {
        builders.put(MagazineView.class.getSimpleName(), this::buildMagazineViewDisplay);
        builders.put(ListView.class.getSimpleName(), this::buildListViewDisplay);
        builders.put(GridView.class.getSimpleName(), this::buildListViewDisplay);
        builders.put(MapView.class.getSimpleName(), this::buildMapViewDisplay);
        builders.put(DisplayView.class.getSimpleName(), this::buildOlapViewDisplay);
    }

    public void selectView(String name) {
        CatalogViewDisplay display = display(name);
        notifier.refreshSelectedView(name);
        selectListeners.forEach(l -> l.accept(display));
    }

    public void refresh() {
        viewDisplayMap.values().forEach(ElementViewDisplay::refresh);
    }

    @Override
    protected void init() {
        super.init();
        sendTarget();
        sendViewList();
        if (viewList.size() > 0)
            selectView(viewList.get(0).name());
    }

    private CatalogViewDisplay display(String name) {
        if (!viewDisplayMap.containsKey(name))
            buildViewDisplay(name);
        return viewDisplayMap.get(name);
    }

    private void buildViewDisplay(String name) {
        ElementView view = viewList.stream().filter(v -> v.name().equals(name)).findFirst().orElse(null);
        builders.get(view.type()).apply(view);
    }

    private void sendTarget() {
        if (provider.target() == null) return;
        notifier.refreshTarget(provider.target().name());
    }

    private void sendViewList() {
        notifier.refreshViewList(ReferenceBuilder.buildCatalogViewList(viewList));
    }

    private CatalogMagazineViewDisplay buildMagazineViewDisplay(ElementView view) {
        CatalogMagazineViewDisplay display = new CatalogMagazineViewDisplay(box);
        registerViewDisplay(display, view);
        add(display);
        display.personifyOnce(idOf(view));
        return display;
    }

    private CatalogListViewDisplay buildListViewDisplay(ElementView view) {
        CatalogListViewDisplay display = new CatalogListViewDisplay(box);
        registerViewDisplay(display, view);
        add(display);
        display.personifyOnce(idOf(view));
        return display;
    }

    private CatalogMapViewDisplay buildMapViewDisplay(ElementView view) {
        CatalogMapViewDisplay display = new CatalogMapViewDisplay(box);
        registerViewDisplay(display, view);
        add(display);
        display.personifyOnce(idOf(view));
        return display;
    }

    private CatalogDisplayViewDisplay buildOlapViewDisplay(ElementView view) {
        CatalogDisplayViewDisplay display = new CatalogDisplayViewDisplay(box);
        registerViewDisplay(display, view);
        add(display);
        display.personifyOnce(idOf(view));
        return display;
    }

    private void registerViewDisplay(CatalogViewDisplay display, ElementView view) {
        display.provider(provider);
        display.onOpenItem(this::openItem);
        display.onOpenItemDialog(this::openItemDialog);
        display.onExecuteItemTask(this::executeTask);
        display.view(view);
        display.onLoading(this::notifyLoading);
        viewDisplayMap.put(view.name(), display);
    }

    private void openItem(OpenItemEvent parameters) {
        openItemListeners.forEach(l -> l.accept(parameters));
    }

    private void openItemDialog(OpenItemDialogEvent event) {
        openItemDialogListeners.forEach(l -> l.accept(event));
    }

    private void executeTask(ExecuteItemTaskEvent event) {
        executeItemTaskListeners.forEach(l -> l.accept(event));
    }

    private void notifyLoading(Boolean loading) {
        loadingListeners.forEach(l -> l.accept(loading));
    }

    private String idOf(ElementView view) {
        return (provider.target() != null ? provider.target().name() : "") + view.name();
    }

}