package io.intino.konos.alexandria.ui.model.mold.stamps;

import io.intino.konos.alexandria.ui.model.Catalog;
import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.mold.Stamp;
import io.intino.konos.alexandria.ui.services.push.UISession;

public class CatalogLink extends Stamp<String> {
	private Catalog catalog;
	private ItemLoader itemLoader;
	private Filter filter;

	public Catalog catalog() {
		return this.catalog;
	}

	public CatalogLink catalog(Catalog catalog) {
		this.catalog = catalog;
		return this;
	}

	@Override
	public String objectValue(Object object, UISession session) {
		return value() != null ? value().value(object, session) : null;
	}

	public boolean filtered() {
		return filter != null;
	}

	public boolean filter(Item source, Item target, UISession session) {
		if (filter == null) return true;
		return filter.filter(source != null ? source.object() : null, target != null ? target.object() : null, session);
	}

	public CatalogLink filter(Filter filter) {
		this.filter = filter;
		return this;
	}

	public boolean openItemOnLoad() {
		return itemLoader != null;
	}

	public String item(Item target, UISession session) {
		return itemLoader != null ? itemLoader.item(target != null ? target.object() : null, session) : null;
	}

	public CatalogLink itemLoader(ItemLoader itemLoader) {
		this.itemLoader = itemLoader;
		return this;
	}

	public interface ItemLoader {
		String item(Object target, UISession session);
	}

	public interface Filter {
		boolean filter(Object sourceObject, Object targetObject, UISession session);
	}
}
