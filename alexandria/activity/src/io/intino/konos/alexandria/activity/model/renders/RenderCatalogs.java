package io.intino.konos.alexandria.activity.model.renders;

import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.model.ElementRender;
import io.intino.konos.alexandria.activity.model.Item;

import java.util.List;

public class RenderCatalogs extends ElementRender {
	private List<Catalog> catalogs;
	private Filter filter = null;

	public List<Catalog> catalogs() {
		return catalogs;
	}

	public RenderCatalogs catalogs(List<Catalog> catalogs) {
		this.catalogs = catalogs;
		return this;
	}

	public boolean filter(Catalog catalog, Element context, Item target, Item item, String username) {
		return filter == null || filter.filter(catalog, context, target, item.object(), username);
	}

	public RenderCatalogs filter(Filter filter) {
		this.filter = filter;
		return this;
	}

	public interface Filter {
		boolean filter(Catalog catalog, Element context, Object target, Object object, String username);
	}
}
