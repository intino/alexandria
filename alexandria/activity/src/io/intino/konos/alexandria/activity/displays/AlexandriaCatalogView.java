package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.providers.CatalogViewDisplayProvider;
import io.intino.konos.alexandria.activity.model.Item;

import java.util.List;

public abstract class AlexandriaCatalogView<N extends AlexandriaDisplayNotifier> extends AlexandriaElementView<N, CatalogViewDisplayProvider> {

	public AlexandriaCatalogView(Box box) {
		super(box);
	}

	public abstract void reset();
	public abstract List<Item> selectedItems();
	public abstract void refreshSelection(List<String> items);
}
