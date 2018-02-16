package io.intino.konos.alexandria.activity.model.catalog.events;

import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

import java.util.List;

public class OpenCatalog extends Open {
	private Catalog catalog = null;
	private ItemsLoader itemsLoader;

	public Catalog catalog() {
		return catalog;
	}

	public OpenCatalog catalog(Catalog catalog) {
		this.catalog = catalog;
		return this;
	}

	public List<String> items(Item target, ActivitySession session) {
		return itemsLoader != null ? itemsLoader.items(catalog, target != null ? target.object() : null, session) : null;
	}

	public OpenCatalog itemsLoader(ItemsLoader itemsLoader) {
		this.itemsLoader = itemsLoader;
		return this;
	}

	public interface ItemsLoader {
		List<String> items(Catalog catalog, Object target, ActivitySession session);
	}
}
