package io.intino.alexandria.ui.displays.providers;

import io.intino.alexandria.ui.displays.AlexandriaNavigator;

public interface TemporalCatalogViewDisplayProvider extends CatalogViewDisplayProvider {
    <N extends AlexandriaNavigator> void configureTemporalNavigator(N navigator);
}

