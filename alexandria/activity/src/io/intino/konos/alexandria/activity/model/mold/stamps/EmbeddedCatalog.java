package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.displays.AlexandriaCatalog;
import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.Stamp;

public class EmbeddedCatalog extends Stamp<String> {
	private Catalog catalog;
	private AlexandriaCatalog display;
	private Filter filter;

	public boolean filter(Element context, Item target, Item item, String username) {
		if (filter == null) return true;
		if (target == null && item == null) return true;
		if (target == null || item == null) return false;
		return filter.filter(context, target.object(), item.object(), username);
	}

	public EmbeddedCatalog filter(Filter filter) {
		this.filter = filter;
		return this;
	}

	public Catalog catalog() {
		return catalog;
	}

	public EmbeddedCatalog catalog(Catalog catalog) {
		this.catalog = catalog;
		return this;
	}

	public AlexandriaCatalog display() {
		return display;
	}

	public EmbeddedCatalog display(AlexandriaCatalog display) {
		this.display = display;
		return this;
	}

	@Override
	public String objectValue(Object object, String username) {
		return null;
	}

	public interface Filter {
		boolean filter(Element context, Object target, Object object, String username);
	}

}
