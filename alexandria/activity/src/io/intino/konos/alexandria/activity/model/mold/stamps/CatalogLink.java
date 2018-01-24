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

	public boolean filtered() {
		return filter != null;
	}

	public boolean filter(Item source, Item target, String username) {
		if (filter == null) return true;
		return filter.filter(source != null ? source.object() : null, target != null ? target.object() : null, username);
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
