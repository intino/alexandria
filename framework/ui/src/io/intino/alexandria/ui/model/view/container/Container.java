package io.intino.alexandria.ui.model.view.container;

import io.intino.alexandria.ui.displays.AlexandriaElementDisplay;
import io.intino.alexandria.ui.model.Element;
import io.intino.alexandria.ui.model.Item;
import io.intino.alexandria.ui.model.Panel;
import io.intino.alexandria.ui.model.View;

import java.util.UUID;

public class Container {
	private DisplayLoader loader;
	private DisplayTypeLoader typeLoader;

	public AlexandriaElementDisplay displayFor(Element element, Item item) {
		return loader != null ? loader.displayFor(element, item != null ? item.object() : null) : null;
	}

	public Container displayLoader(DisplayLoader loader) {
		this.loader = loader;
		return this;
	}

	public Class<? extends AlexandriaElementDisplay> displayTypeFor(Element element, Item item) {
		return typeLoader != null ? typeLoader.typeFor(element, item != null ? item.object() : null) : null;
	}

	public Container displayTypeLoader(DisplayTypeLoader typeLoader) {
		this.typeLoader = typeLoader;
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

	public interface DisplayLoader {
		AlexandriaElementDisplay displayFor(Element element, Object object);
	}

	public interface DisplayTypeLoader {
		Class<? extends AlexandriaElementDisplay> typeFor(Element element, Object object);
	}
}
