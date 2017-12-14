package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaPanelCatalogViewNotifier;
import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.TemporalCatalog;
import io.intino.konos.alexandria.activity.model.panel.View;
import io.intino.konos.alexandria.activity.model.renders.RenderCatalogs;

import java.util.Optional;

public class AlexandriaPanelCatalogView extends AlexandriaPanelView<AlexandriaPanelCatalogViewNotifier> {

	public AlexandriaPanelCatalogView(Box box) {
		super(box);
	}

	@Override
	protected void init() {
		super.init();
		createCatalogDisplay();
	}

	private void createCatalogDisplay() {
		View rawView = view().raw();
		RenderCatalogs render = rawView.render();
		Catalog catalog = render.catalogs().get(0);
		buildDisplay(catalog).ifPresent(display -> {
			display.filter(item -> render.filter(catalog, context(), target(), (Item) item));
			display.target(target());
			display.elementDisplayManager(provider().elementDisplayManager());
			display.catalog(catalog);
			display.onLoading(value -> notifyLoading((Boolean) value));
			display.onOpenItem(params -> notifyOpenItem((OpenItemEvent) params));
			add(display);
			display.personifyOnce(id());
		});
	}

	private Optional<AlexandriaAbstractCatalog> buildDisplay(Catalog catalog) {
		if (catalog instanceof TemporalCatalog)
			return Optional.of((((TemporalCatalog)catalog).type() == TemporalCatalog.Type.Range) ? new AlexandriaTemporalRangeCatalog(box) : new AlexandriaTemporalTimeCatalog(box));
		return Optional.of(new AlexandriaCatalog(box));
	}

}