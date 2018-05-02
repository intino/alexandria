package io.intino.konos.alexandria.ui.model.view;

import io.intino.konos.alexandria.ui.model.View;
import io.intino.konos.alexandria.ui.model.view.container.Container;
import io.intino.konos.alexandria.ui.model.view.container.DisplayContainer;

public class ContainerView extends View {
	private Container container;

	public <C extends Container> C container() {
		return (C) container;
	}

	public ContainerView container(Container container) {
		this.container = container;
		return this;
	}

	public boolean hideNavigator() {
		if (container instanceof DisplayContainer) return ((DisplayContainer)container).hideNavigator();
		return true;
	}
}
