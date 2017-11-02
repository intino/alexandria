package io.intino.konos.alexandria.framework.box.model.mold.stamps;

import io.intino.konos.alexandria.framework.box.displays.AlexandriaCatalogDisplay;
import io.intino.konos.alexandria.framework.box.model.Element;
import io.intino.konos.alexandria.framework.box.model.Item;
import io.intino.konos.alexandria.framework.box.model.Catalog;
import io.intino.konos.alexandria.framework.box.model.mold.Stamp;

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

	public AlexandriaCatalogDisplay display() {
		return displayBuilder != null ? displayBuilder.display(catalog) : null;
	}

	public EmbeddedCatalog displayBuilder(CatalogDisplayBuilder builder) {
		this.displayBuilder = builder;
		return this;
	}

	@Override
	public String value(Object object, String username) {
		return null;
	}

	public interface Filter {
		boolean filter(Element context, Object target, Object object);
	}

	public interface CatalogDisplayBuilder {
		<CD extends AlexandriaCatalogDisplay> CD display(Catalog catalog);
	}
}
