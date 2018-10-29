package io.intino.alexandria.ui.model.view.container;

import io.intino.alexandria.ui.displays.AlexandriaAbstractCatalog;
import io.intino.alexandria.ui.model.Catalog;
import io.intino.alexandria.ui.model.Element;
import io.intino.alexandria.ui.model.Item;
import io.intino.alexandria.ui.services.push.UISession;

public class CatalogContainer extends Container {
	private Catalog catalog;
	private Filter filter = null;
	private Loader loader;

	public Catalog catalog() {
		return catalog;
	}

	public CatalogContainer catalog(Catalog catalog) {
		this.catalog = catalog;
		return this;
	}

	public AlexandriaAbstractCatalog display(Catalog catalog, UISession session) {
		return loader != null ? loader.load(catalog, session) : null;
	}

	public CatalogContainer displayLoader(Loader loader) {
		this.loader = loader;
		return this;
	}

	public boolean filter(Catalog catalog, Element context, Item target, Item item, UISession session) {
		if (target == null && item == null) return true;
		if (target == null || item == null) return true;
		return filter == null || filter.filter(catalog, context, target.object(), item.object(), session);
	}

	public CatalogContainer filter(Filter filter) {
		this.filter = filter;
		return this;
	}

	public interface Filter {
		boolean filter(Catalog catalog, Element context, Object target, Object object, UISession session);
	}

	public interface Loader {
		AlexandriaAbstractCatalog load(Catalog catalog, UISession session);
	}
}
