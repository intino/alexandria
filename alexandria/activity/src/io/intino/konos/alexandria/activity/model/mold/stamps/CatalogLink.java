package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.User;

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

	public boolean filter(Item source, Item target, User user) {
		if (filter == null) return true;
		return filter.filter(source != null ? source.object() : null, target != null ? target.object() : null, user);
	}

	public CatalogLink filter(Filter filter) {
		this.filter = filter;
		return this;
	}

	@Override
	public String objectValue(Object object, User user) {
		return value() != null ? value().value(object, user) : null;
	}

	public interface Filter {
		boolean filter(Object sourceObject, Object targetObject, User user);
	}
}
