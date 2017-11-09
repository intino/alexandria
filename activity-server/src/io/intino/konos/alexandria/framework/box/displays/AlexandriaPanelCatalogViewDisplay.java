package io.intino.konos.alexandria.framework.box.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.framework.box.displays.notifiers.AlexandriaPanelCatalogViewDisplayNotifier;
import io.intino.konos.alexandria.framework.box.model.Catalog;
import io.intino.konos.alexandria.framework.box.model.TemporalCatalog;
import io.intino.konos.alexandria.framework.box.model.panel.View;
import io.intino.konos.alexandria.framework.box.model.renders.RenderCatalog;

import java.util.Optional;

public class AlexandriaPanelCatalogViewDisplay extends AlexandriaPanelViewDisplay<AlexandriaPanelCatalogViewDisplayNotifier> {

	public AlexandriaPanelCatalogViewDisplay(Box box) {
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
			display.filter(item -> render.filter(context(), target(), (io.intino.konos.alexandria.framework.box.model.Item) item));
			display.target(target());
			display.elementDisplayManager(provider().elementDisplayManager());
			display.catalog(catalog);
			display.onLoading(value -> notifyLoading((Boolean) value));
			display.onOpenItem(params -> notifyOpenItem((OpenItemEvent) params));
			add(display);
			display.personifyOnce(id());
		});
	}

	private Optional<AlexandriaAbstractCatalogDisplay> buildDisplay(Catalog catalog) {
		if (catalog instanceof TemporalCatalog)
			return Optional.of((((TemporalCatalog)catalog).type() == TemporalCatalog.Type.Range) ? new AlexandriaTemporalRangeCatalogDisplay(box) : new AlexandriaTemporalTimeCatalogDisplay(box));
		return Optional.of(new AlexandriaCatalogDisplay(box));
	}

}