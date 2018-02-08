package io.intino.konos.alexandria.activity.model.renders;

import io.intino.konos.alexandria.activity.displays.AlexandriaAbstractCatalog;
import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.model.ElementRender;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.services.push.User;

import java.util.List;

public class RenderCatalogs extends ElementRender {
	private List<Catalog> catalogs;
	private Filter filter = null;
	private Loader loader;

	public List<Catalog> catalogs() {
		return catalogs;
	}

	public RenderCatalogs catalogs(List<Catalog> catalogs) {
		this.catalogs = catalogs;
		return this;
	}

	public AlexandriaAbstractCatalog display(Catalog catalog, User user) {
		return loader != null ? loader.load(catalog, user) : null;
	}

	public RenderCatalogs displayLoader(Loader loader) {
		this.loader = loader;
		return this;
	}

	public boolean filter(Catalog catalog, Element context, Item target, Item item, User user) {
		if (target == null && item == null) return true;
		if (target == null || item == null) return false;
		return filter == null || filter.filter(catalog, context, target.object(), item.object(), user);
	}

	public RenderCatalogs filter(Filter filter) {
		this.filter = filter;
		return this;
	}

	public interface Filter {
		boolean filter(Catalog catalog, Element context, Object target, Object object, User user);
	}

	public interface Loader {
		AlexandriaAbstractCatalog load(Catalog catalog, User user);
	}
}
