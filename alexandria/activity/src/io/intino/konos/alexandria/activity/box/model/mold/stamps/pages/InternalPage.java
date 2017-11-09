package io.intino.konos.alexandria.activity.box.model.mold.stamps.pages;

import io.intino.konos.alexandria.activity.box.model.Item;
import io.intino.konos.alexandria.activity.box.model.mold.stamps.Page;

public class InternalPage extends Page {
	private PathResolver resolver;

	public String path(Item item) {
		return item != null && resolver != null ? resolver.path(item.name()) : null;
	}

	public InternalPage path(PathResolver pathResolver) {
		this.resolver = pathResolver;
		return this;
	}

	public interface PathResolver {
		String path(String item);
	}
}
