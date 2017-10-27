package io.intino.konos.server.activity.displays.catalogs.views;

import io.intino.konos.server.activity.displays.catalogs.providers.CatalogViewDisplayProvider;
import io.intino.konos.server.activity.displays.elements.ElementViewDisplay;

public interface CatalogViewDisplay extends ElementViewDisplay<CatalogViewDisplayProvider> {
	void reset();
}
