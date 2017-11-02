package io.intino.alexandria.framework.box.model.renders;

import io.intino.alexandria.framework.box.model.Element;
import io.intino.alexandria.framework.box.model.ElementRender;
import io.intino.alexandria.framework.box.model.Item;
import io.intino.alexandria.framework.box.model.Catalog;
import io.intino.alexandria.framework.box.model.layout.ElementOption;

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
