package io.intino.alexandria.ui.model;

import io.intino.alexandria.ui.model.view.container.*;
import io.intino.alexandria.ui.services.push.UISession;

public class View {
	private String name;
	private String label;
	private Layout layout = Layout.Tab;
	private Hidden hidden = null;
	private int width = -1;
	private Container container;

	public enum Layout {
		Summary, Tab, LeftFixed, RightFixed
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
		Container container = container();
		if (container instanceof MoldContainer) return ((MoldContainer) container).mold();
		return (container instanceof CollectionContainer) ? ((CollectionContainer)container).mold() : null;
	}

	public <C extends Container> C container() {
		return (C) container;
	}

	public View container(Container container) {
		this.container = container;
		return this;
	}

	public boolean hideNavigator() {
		if (container instanceof DisplayContainer) return ((DisplayContainer)container).hideNavigator();
		return !(container instanceof ListContainer || container instanceof GridContainer || container instanceof MapContainer || container instanceof MagazineContainer);
	}

	public interface Hidden {
		boolean hidden(Object object, UISession session);
	}
}
