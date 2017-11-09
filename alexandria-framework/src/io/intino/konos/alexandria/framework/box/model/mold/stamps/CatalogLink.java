package io.intino.konos.alexandria.framework.box.model.mold.stamps;

import io.intino.konos.alexandria.framework.box.model.Item;
import io.intino.konos.alexandria.framework.box.model.Catalog;
import io.intino.konos.alexandria.framework.box.model.mold.Stamp;

public class CatalogLink extends Stamp<String> {
	private Value<String> title;
	private Catalog catalog;
	private Filter filter;

	public String title(Item item, String username) {
		if (item == null) return null;
		return objectTitle(item.object(), username);
	}

	public String objectTitle(Object object, String username) {
		return title != null ? title.value(object, username) : null;
	}

	public CatalogLink title(Value<String> title) {
		this.title = title;
		return this;
	}

	public Catalog catalog() {
		return this.catalog;
	}

	public CatalogLink catalog(Catalog catalog) {
		this.catalog = catalog;
		return this;
	}

	public boolean filter(Item source, Item target) {
		if (filter == null) return true;
		if (source == null && target == null) return true;
		if (source == null || target == null) return false;
		return filter.filter(source.object(), target.object());
	}

	public CatalogLink filter(Filter filter) {
		this.filter = filter;
		return this;
	}

	@Override
	public String objectValue(Object object, String username) {
		return null;
	}

	public interface Filter {
		boolean filter(Object sourceObject, Object targetObject);
	}
}
