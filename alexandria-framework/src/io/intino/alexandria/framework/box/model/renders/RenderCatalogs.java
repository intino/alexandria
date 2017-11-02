package io.intino.alexandria.framework.box.model.renders;

import io.intino.alexandria.framework.box.model.ElementRender;
import io.intino.alexandria.framework.box.model.Catalog;
import io.intino.alexandria.framework.box.model.layout.ElementOption;

import java.util.List;

import static java.util.Collections.emptyList;

public class RenderCatalogs extends ElementRender {
	private CatalogProvider provider;

	public RenderCatalogs(ElementOption option) {
		super(option);
	}

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
