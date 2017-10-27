package io.intino.konos.server.activity.displays.panels.views;

import io.intino.konos.Box;
import io.intino.konos.server.activity.displays.catalogs.AbstractCatalogDisplay;
import io.intino.konos.server.activity.displays.catalogs.CatalogDisplay;
import io.intino.konos.server.activity.displays.catalogs.TemporalRangeCatalogDisplay;
import io.intino.konos.server.activity.displays.catalogs.TemporalTimeCatalogDisplay;
import io.intino.konos.server.activity.displays.catalogs.model.Catalog;
import io.intino.konos.server.activity.displays.catalogs.model.TemporalCatalog;
import io.intino.konos.server.activity.displays.elements.model.renders.RenderCatalog;
import io.intino.konos.server.activity.displays.panels.model.View;

import java.util.Optional;

public class PanelCatalogViewDisplay extends PanelViewDisplay<PanelCatalogViewDisplayNotifier> {

	public PanelCatalogViewDisplay(Box box) {
		super(box);
	}

	@Override
	protected void init() {
		super.init();
		createCatalogDisplay();
	}

	private void createCatalogDisplay() {
		View rawView = view().raw();
		RenderCatalog render = rawView.render();
		Catalog catalog = render.catalog();
		buildDisplay(catalog).ifPresent(display -> {
			display.filter(item -> render.filter(context(), target(), (io.intino.konos.server.activity.displays.elements.model.Item) item));
			display.target(target());
			display.elementDisplayManager(provider().elementDisplayManager());
			display.catalog(catalog);
			display.onLoading(value -> notifyLoading((Boolean) value));
			display.onOpenItem(params -> notifyOpenItem((OpenItemEvent) params));
			add(display);
			display.personifyOnce(id());
		});
	}

	private Optional<AbstractCatalogDisplay> buildDisplay(Catalog catalog) {
		if (catalog instanceof TemporalCatalog)
			return Optional.of((((TemporalCatalog)catalog).type() == TemporalCatalog.Type.Range) ? new TemporalRangeCatalogDisplay(box) : new TemporalTimeCatalogDisplay(box));
		return Optional.of(new CatalogDisplay(box));
	}

}