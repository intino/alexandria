package io.intino.alexandria.ui.model.mold.stamps.pages;

import io.intino.alexandria.ui.model.mold.stamps.Page;
import io.intino.alexandria.ui.model.Item;

import java.net.URL;

public class ExternalPage extends Page {
	private UrlResolver resolver;

	public URL url(Item item) {
		return item != null && resolver != null ? resolver.url(item.name()) : null;
	}

	public ExternalPage url(UrlResolver urlResolver) {
		this.resolver = urlResolver;
		return this;
	}

	public interface UrlResolver {
		URL url(String item);
	}
}
