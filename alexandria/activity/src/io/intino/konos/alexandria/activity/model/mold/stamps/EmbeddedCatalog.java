package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.displays.AlexandriaCatalog;
import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.mold.Stamp;

public class EmbeddedCatalog extends Stamp<String> {
	private Catalog catalog;
	private Filter filter;
	private CatalogDisplayBuilder displayBuilder;

	public Catalog catalog() {
		return this.catalog;
	}

	public EmbeddedCatalog catalog(Catalog catalog) {
		this.catalog = catalog;
		return this;
	}

	public boolean filter(Element context, Item target, Item item) {
		if (filter == null) return true;
		if (target == null && item == null) return true;
		if (target == null || item == null) return false;
		return filter.filter(context, target.object(), item.object());
	}

	public EmbeddedCatalog filter(Filter filter) {
		this.filter = filter;
		return this;
	}

	public AlexandriaCatalog display() {
		return displayBuilder != null ? displayBuilder.display(catalog) : null;
	}

	public EmbeddedCatalog displayBuilder(CatalogDisplayBuilder builder) {
		this.displayBuilder = builder;
		return this;
	}

	@Override
	public String objectValue(Object object, String username) {
		return null;
	}

	public interface Filter {
		boolean filter(Element context, Object target, Object object);
	}

	public interface CatalogDisplayBuilder {
		<CD extends AlexandriaCatalog> CD display(Catalog catalog);
	}
}
