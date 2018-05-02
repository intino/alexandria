package io.intino.konos.alexandria.ui.model;

import io.intino.konos.alexandria.ui.model.view.CatalogView;
import io.intino.konos.alexandria.ui.model.view.ContainerView;
import io.intino.konos.alexandria.ui.model.view.container.Container;
import io.intino.konos.alexandria.ui.model.view.container.MoldContainer;
import io.intino.konos.alexandria.ui.services.push.UISession;

public class View {
	private String name;
	private String label;
	private Layout layout = Layout.Tab;
	private Hidden hidden = null;
	private int width;

	public enum Layout {
		Tab, LeftFixed, RightFixed
	}

	public String name() {
		return name;
	}

	public View name(String name) {
		this.name = name;
		return this;
	}

	public String label() {
		return label;
	}

	public View label(String label) {
		this.label = label;
		return this;
	}

	public Layout layout() {
		return layout;
	}

	public View layout(Layout layout) {
		this.layout = layout;
		return this;
	}

	public boolean hidden(Item item, UISession session) {
		return hidden != null && hidden.hidden(item != null ? item.object() : null, session);
	}

	public View hidden(Hidden hidden) {
		this.hidden = hidden;
		return this;
	}

	public int width() {
		return width;
	}

	public View width(int width) {
		this.width = width;
		return this;
	}

	public Mold mold() {
		if (this instanceof ContainerView) {
			Container container = ((ContainerView)this).container();
			if (container instanceof MoldContainer) return ((MoldContainer) container).mold();
			return null;
		}
		return (this instanceof CatalogView) ? ((CatalogView)this).mold() : null;
	}

	public interface Hidden {
		boolean hidden(Object object, UISession session);
	}
}
