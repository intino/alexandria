package io.intino.konos.alexandria.activity.model.renders;

import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.ElementRender;

import java.util.List;

public class RenderCatalogs extends ElementRender {
	private List<Catalog> catalogs;

	public List<Catalog> catalogs() {
		return catalogs;
	}

	public RenderCatalogs catalogs(List<Catalog> catalogs) {
		this.catalogs = catalogs;
		return this;
	}

}
