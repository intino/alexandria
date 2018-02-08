package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.displays.AlexandriaAbstractCatalog;
import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.User;

import java.util.ArrayList;
import java.util.List;

public class EmbeddedCatalog extends Stamp<String> {
	private List<String> views = new ArrayList<>();
	private Catalog catalog;
	private CatalogDisplayBuilder catalogDisplayBuilder;
	private Filter filter;

	public boolean filter(Element context, Item target, Item item, User user) {
		if (filter == null) return true;
		if (target == null && item == null) return true;
		if (target == null || item == null) return false;
		return filter.filter(context, target.object(), item.object(), user);
	}

	public EmbeddedCatalog filter(Filter filter) {
		this.filter = filter;
		return this;
	}

	public List<String> views() {
		return views;
	}

	public EmbeddedCatalog views(List<String> views) {
		this.views = views;
		return this;
	}

	public Catalog catalog() {
		return this.catalog;
	}

	public EmbeddedCatalog catalog(Catalog catalog) {
		this.catalog = catalog;
		return this;
	}

	public AlexandriaAbstractCatalog createCatalog(User user) {
		AlexandriaAbstractCatalog catalog = catalogDisplayBuilder != null ? catalogDisplayBuilder.build(user) : null;
		if (catalog == null) return null;
		catalog.enabledViews(views);
		return catalog;
	}

	public EmbeddedCatalog catalogDisplayBuilder(CatalogDisplayBuilder builder) {
		this.catalogDisplayBuilder = builder;
		return this;
	}

	@Override
	public String objectValue(Object object, User user) {
		return null;
	}

	public interface CatalogDisplayBuilder {
		AlexandriaAbstractCatalog build(User user);
	}

	public interface Filter {
		boolean filter(Element context, Object target, Object object, User user);
	}

}
