package io.intino.konos.alexandria.activity.model.catalog.events;

import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

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

	public boolean filter(Item source, Item target, ActivitySession session) {
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

	public String item(Item target, ActivitySession session) {
		return itemLoader != null ? itemLoader.item(catalog, target != null ? target.object() : null, session) : null;
	}

	public OpenCatalog itemLoader(ItemLoader itemLoader) {
		this.itemLoader = itemLoader;
		return this;
	}

	public interface ItemLoader {
		String item(Catalog catalog, Object target, ActivitySession session);
	}

	public interface Filter {
		boolean filter(Catalog catalog, Object sourceObject, Object targetObject, ActivitySession session);
	}
}
