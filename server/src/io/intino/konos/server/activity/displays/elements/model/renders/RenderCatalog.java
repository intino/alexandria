package io.intino.konos.server.activity.displays.elements.model.renders;

import io.intino.konos.server.activity.displays.catalogs.model.Catalog;
import io.intino.konos.server.activity.displays.elements.model.Element;
import io.intino.konos.server.activity.displays.elements.model.ElementRender;
import io.intino.konos.server.activity.displays.elements.model.Item;
import io.intino.konos.server.activity.displays.layouts.model.ElementOption;

public class RenderCatalog extends ElementRender {
	private Catalog catalog;
	private Filter filter = null;

	public RenderCatalog(ElementOption option) {
		super(option);
	}

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
