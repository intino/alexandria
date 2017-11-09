package io.intino.konos.alexandria.activity.model.renders;

import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.model.ElementRender;
import io.intino.konos.alexandria.activity.model.Item;

public class RenderCatalog extends ElementRender {
	private Catalog catalog;
	private Filter filter = null;

	public Catalog catalog() {
		return catalog;
	}

	public RenderCatalog catalog(Catalog catalog) {
		this.catalog = catalog;
		return this;
	}

	public boolean filter(Element context, Item target, Item item) {
		return filter == null || filter.filter(context, target, item.object());
	}

	public RenderCatalog filter(Filter filter) {
		this.filter = filter;
		return this;
	}

	public interface Filter {
		boolean filter(Element context, Object target, Object object);
	}
}
