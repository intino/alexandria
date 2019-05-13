package io.intino.alexandria.ui.model.catalog.events;

import io.intino.alexandria.ui.model.Catalog;
import io.intino.alexandria.ui.model.Item;
import io.intino.alexandria.ui.services.push.UISession;

public class OpenCatalog extends Open {
	private Catalog catalog = null;
	private ItemLoader itemLoader;
	private Filter filter;

	public Catalog catalog() {
		return catalog;
	}

	public OpenCatalog catalog(Catalog catalog) {
		this.catalog = catalog;
		return this;
	}

	public boolean filtered() {
		return filter != null;
	}

	public boolean filter(Item source, Item target, UISession session) {
		if (filter == null) return true;
		return filter.filter(catalog, source != null ? source.object() : null, target != null ? target.object() : null, session);
	}

	public OpenCatalog filter(Filter filter) {
		this.filter = filter;
		return this;
	}

	public boolean openItemOnLoad() {
		return itemLoader != null;
	}

	public String item(Item target, UISession session) {
		return itemLoader != null ? itemLoader.item(catalog, target != null ? target.object() : null, session) : null;
	}

	public OpenCatalog itemLoader(ItemLoader itemLoader) {
		this.itemLoader = itemLoader;
		return this;
	}

	public interface ItemLoader {
		String item(Catalog catalog, Object target, UISession session);
	}

	public interface Filter {
		boolean filter(Catalog catalog, Object sourceObject, Object targetObject, UISession session);
	}
}
