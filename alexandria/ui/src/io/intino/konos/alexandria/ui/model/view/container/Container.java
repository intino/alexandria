package io.intino.konos.alexandria.ui.model.view.container;

import io.intino.konos.alexandria.ui.displays.AlexandriaDisplay;
import io.intino.konos.alexandria.ui.model.Element;
import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.Panel;
import io.intino.konos.alexandria.ui.model.View;

import java.util.UUID;

public class Container {
	private DisplayTypeLoader loader;

	public Class<? extends AlexandriaDisplay> displayTypeFor(Element element, Item item) {
		return loader != null ? loader.typeFor(element, item != null ? item.object() : null) : null;
	}

	public Container displayTypeLoader(DisplayTypeLoader loader) {
		this.loader = loader;
		return this;
	}

	public Element element() {
		if (this instanceof PanelContainer) return ((PanelContainer)this).panel();
		if (this instanceof CatalogContainer) return ((CatalogContainer)this).catalog();
		if (this instanceof MoldContainer) return ((MoldContainer)this).mold();
		if (this instanceof DisplayContainer) {
			Panel panel = new Panel();
			panel.views().add(new View().container(this).name(UUID.randomUUID().toString()).layout(View.Layout.Tab));
			return panel;
		}
		return null;
	}

	public interface DisplayTypeLoader {
		Class<? extends AlexandriaDisplay> typeFor(Element element, Object object);
	}
}
