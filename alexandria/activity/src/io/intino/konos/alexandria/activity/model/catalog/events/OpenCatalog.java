package io.intino.konos.alexandria.activity.model.catalog.events;

import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.services.push.User;

public class OpenCatalog extends Open {
	private Catalog catalog = null;
	private ItemLoader itemLoader;

	public Catalog catalog() {
		return catalog;
	}

	public OpenCatalog catalog(Catalog catalog) {
		this.catalog = catalog;
		return this;
	}

	public Item item(Item target, User user) {
		return itemLoader != null ? itemLoader.item(catalog, target != null ? target.object() : null, user) : null;
	}

	public OpenCatalog itemLoader(ItemLoader itemLoader) {
		this.itemLoader = itemLoader;
		return this;
	}

	public interface ItemLoader {
		Item item(Catalog catalog, Object target, User user);
	}
}
