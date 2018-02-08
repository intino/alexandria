package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaPanelCatalogViewNotifier;
import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.panel.View;
import io.intino.konos.alexandria.activity.model.renders.RenderCatalogs;

public class AlexandriaPanelCatalogView extends AlexandriaPanelView<AlexandriaPanelCatalogViewNotifier> {
	private AlexandriaAbstractCatalog catalogDisplay = null;

	public AlexandriaPanelCatalogView(Box box) {
		super(box);
	}

	@Override
	protected void init() {
		super.init();
		createCatalogDisplay();
	}

	public <E extends AlexandriaElementDisplay> E catalogDisplay() {
		return (E) catalogDisplay;
	}

	private void createCatalogDisplay() {
		View rawView = view().raw();
		RenderCatalogs render = rawView.render();
		Catalog catalog = render.catalogs().get(0);
		catalogDisplay = render.display(catalog, user());
		if (catalogDisplay == null) return;
		sendDisplayType(catalogDisplay);
		catalogDisplay.staticFilter(item -> render.filter(catalog, context(), target(), (Item) item, user()));
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