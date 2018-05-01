package io.intino.konos.alexandria.ui.displays.providers;

import io.intino.konos.alexandria.ui.displays.AlexandriaNavigator;

public interface TemporalCatalogViewDisplayProvider extends CatalogViewDisplayProvider {
    <N extends AlexandriaNavigator> void configureTemporalNavigator(N navigator);
}

