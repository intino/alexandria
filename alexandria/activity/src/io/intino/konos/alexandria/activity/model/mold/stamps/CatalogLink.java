package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.mold.Stamp;

public class CatalogLink extends Stamp<String> {
	private Catalog catalog;
	private Filter filter;

	public Catalog catalog() {
		return this.catalog;
	}

	public CatalogLink catalog(Catalog catalog) {
		this.catalog = catalog;
		return this;
	}

	public boolean filter(Item source, Item target, String username) {
		if (filter == null) return true;
		if (source == null && target == null) return true;
		if (source == null || target == null) return false;
		return filter.filter(source.object(), target.object(), username);
	}

	public CatalogLink filter(Filter filter) {
		this.filter = filter;
		return this;
	}

	@Override
	public String objectValue(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

	public interface Filter {
		boolean filter(Object sourceObject, Object targetObject, String username);
	}
}
