package io.intino.konos.server.activity.displays.catalogs;

import io.intino.konos.Box;
import io.intino.konos.server.activity.displays.catalogs.model.Catalog;
import io.intino.konos.server.activity.displays.elements.model.ItemList;

public class CatalogDisplay<DN extends CatalogDisplayNotifier> extends AbstractCatalogDisplay<Catalog, DN> {

	public CatalogDisplay(Box box) {
		super(box);
	}

	@Override
	protected ItemList itemList(String condition) {
		ItemList itemList = element().items(condition, username());
		applyFilter(itemList);
		return itemList;
	}

}
