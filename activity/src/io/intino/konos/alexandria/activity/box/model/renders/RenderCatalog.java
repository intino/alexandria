package io.intino.konos.alexandria.activity.box.model.renders;

import io.intino.konos.alexandria.activity.box.model.Catalog;
import io.intino.konos.alexandria.activity.box.model.Element;
import io.intino.konos.alexandria.activity.box.model.ElementRender;
import io.intino.konos.alexandria.activity.box.model.Item;

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
