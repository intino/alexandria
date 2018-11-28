package io.intino.alexandria.ui.model.mold.stamps.pages;

import io.intino.alexandria.ui.model.mold.stamps.Page;
import io.intino.alexandria.ui.model.Item;

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
