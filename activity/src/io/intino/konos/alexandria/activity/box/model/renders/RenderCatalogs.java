package io.intino.konos.alexandria.activity.box.model.renders;

import io.intino.konos.alexandria.activity.box.model.Catalog;
import io.intino.konos.alexandria.activity.box.model.ElementRender;

import java.util.List;

import static java.util.Collections.emptyList;

public class RenderCatalogs extends ElementRender {
	private CatalogProvider provider;

	public List<Catalog> catalogs() {
		return provider != null ? provider.catalogs() : emptyList();
	}

	public RenderCatalogs filter(CatalogProvider provider) {
		this.provider = provider;
		return this;
	}

    public interface CatalogProvider {
		List<Catalog> catalogs();
	}
}
