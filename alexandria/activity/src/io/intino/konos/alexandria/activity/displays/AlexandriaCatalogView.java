package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.activity.displays.providers.CatalogViewDisplayProvider;
import io.intino.konos.alexandria.activity.model.Item;

import java.util.List;

public interface AlexandriaCatalogView extends AlexandriaElementView<CatalogViewDisplayProvider> {
	void reset();
	List<Item> selectedItems();
}
