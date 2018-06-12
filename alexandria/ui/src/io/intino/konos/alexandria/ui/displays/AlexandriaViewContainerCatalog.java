package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.displays.events.OpenItemEvent;
import io.intino.konos.alexandria.ui.displays.notifiers.AlexandriaViewContainerCatalogNotifier;
import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.view.container.CatalogContainer;
import io.intino.konos.alexandria.ui.schemas.DisplayInfo;


public class AlexandriaViewContainerCatalog extends AlexandriaViewContainer<AlexandriaViewContainerCatalogNotifier> {

	private AlexandriaAbstractCatalog catalogDisplay = null;

	public AlexandriaViewContainerCatalog(Box box) {
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
		CatalogContainer container = view().container();
		io.intino.konos.alexandria.ui.model.Catalog catalog = container.catalog();
		catalogDisplay = container.display(catalog, session());
		if (catalogDisplay == null) return;
		sendDisplayType();
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

	private void sendDisplayType() {
		notifier.displayInfo(new DisplayInfo().type(catalogDisplay.name()).elementType(typeOf(catalogDisplay.element())));
	}

}