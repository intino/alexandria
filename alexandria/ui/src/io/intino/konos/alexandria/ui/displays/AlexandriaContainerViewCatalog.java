package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.displays.events.OpenItemEvent;
import io.intino.konos.alexandria.ui.displays.notifiers.AlexandriaContainerViewCatalogNotifier;
import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.view.ContainerView;
import io.intino.konos.alexandria.ui.model.view.container.CatalogContainer;


public class AlexandriaContainerViewCatalog extends AlexandriaContainerView<AlexandriaContainerViewCatalogNotifier> {
    private AlexandriaAbstractCatalog catalogDisplay = null;

    public AlexandriaContainerViewCatalog(Box box) {
        super(box);
    }

    @Override
    protected void init() {
        super.init();
        createCatalogDisplay();
    }

    @Override
    public void refresh() {
        super.refresh();
        if (catalogDisplay != null) catalogDisplay.forceRefresh();
    }

    public <E extends AlexandriaElementDisplay> E catalogDisplay() {
        return (E) catalogDisplay;
    }

    private void createCatalogDisplay() {
        ContainerView view = view();
        CatalogContainer container = view.container();
        io.intino.konos.alexandria.ui.model.Catalog catalog = container.catalog();
        catalogDisplay = container.display(catalog, session());
        if (catalogDisplay == null) return;
        sendDisplayType(catalogDisplay);
        catalogDisplay.staticFilter(item -> container.filter(catalog, context(), target(), (Item) item, session()));
        catalogDisplay.target(target());
        catalogDisplay.elementDisplayManager(provider().elementDisplayManager());
        catalogDisplay.catalog(catalog);
        catalogDisplay.onLoading(value -> notifyLoading((Boolean) value));
        catalogDisplay.onOpenItem(params -> notifyOpenItem((OpenItemEvent) params));
        catalogDisplay.onOpenElement(params -> notifyOpenItem((OpenItemEvent) params));
        add(catalogDisplay);
        catalogDisplay.personifyOnce(id());
    }

    private void sendDisplayType(AlexandriaDisplay display) {
        notifier.displayType(display.name());
    }
}