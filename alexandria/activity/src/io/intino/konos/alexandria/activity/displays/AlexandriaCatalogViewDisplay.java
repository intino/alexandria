package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.activity.displays.providers.CatalogViewDisplayProvider;
import io.intino.konos.alexandria.activity.model.catalog.Scope;

public interface AlexandriaCatalogViewDisplay extends AlexandriaElementViewDisplay<CatalogViewDisplayProvider> {
	void reset();
}
