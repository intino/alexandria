package io.intino.konos.server.activity.displays.molds.model.stamps.pages;

import io.intino.konos.server.activity.displays.elements.model.Item;
import io.intino.konos.server.activity.displays.molds.model.stamps.Page;

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
