package io.intino.konos.alexandria.ui.model.mold.stamps;

import io.intino.konos.alexandria.ui.displays.AlexandriaAbstractCatalog;
import io.intino.konos.alexandria.ui.model.Catalog;
import io.intino.konos.alexandria.ui.model.Element;
import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.mold.Stamp;
import io.intino.konos.alexandria.ui.services.push.UISession;

import java.util.ArrayList;
import java.util.List;

public class EmbeddedCatalog extends Stamp<String> {
	private List<String> views = new ArrayList<>();
	private Catalog catalog;
	private CatalogDisplayBuilder catalogDisplayBuilder;
	private Filter filter;
	private int maxItems = -1;

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

	public AlexandriaAbstractCatalog createCatalog(UISession session) {
		AlexandriaAbstractCatalog catalog = catalogDisplayBuilder != null ? catalogDisplayBuilder.build(session) : null;
		if (catalog == null) return null;
		catalog.enabledViews(views);
		if (maxItems() > 0) catalog.maxItems(maxItems());
		return catalog;
	}

	public EmbeddedCatalog catalogDisplayBuilder(CatalogDisplayBuilder builder) {
		this.catalogDisplayBuilder = builder;
		return this;
	}

	public boolean filter(Element context, Item target, Item item, UISession session) {
		if (filter == null) return true;
		if (target == null && item == null) return true;
		if (target == null || item == null) return false;
		return filter.filter(context, target.object(), item.object(), session);
	}

	public EmbeddedCatalog filter(Filter filter) {
		this.filter = filter;
		return this;
	}

	public int maxItems() {
		return maxItems;
	}

	public EmbeddedCatalog maxItems(int maxItems) {
		this.maxItems = maxItems;
		return this;
	}

	@Override
	public String objectValue(Object object, UISession session) {
		return null;
	}

	public interface CatalogDisplayBuilder {
		AlexandriaAbstractCatalog build(UISession session);
	}

	public interface Filter {
		boolean filter(Element context, Object target, Object object, UISession session);
	}

}
