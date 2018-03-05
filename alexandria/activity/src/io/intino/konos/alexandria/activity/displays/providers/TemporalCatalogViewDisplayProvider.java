package io.intino.konos.alexandria.activity.displays.providers;

import io.intino.konos.alexandria.activity.displays.AlexandriaNavigator;

public interface TemporalCatalogViewDisplayProvider extends CatalogViewDisplayProvider {
    <N extends AlexandriaNavigator> void configureTemporalNavigator(N navigator);
}

