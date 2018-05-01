package io.intino.konos.alexandria.ui.model.views;

import io.intino.konos.alexandria.ui.model.View;
import io.intino.konos.alexandria.ui.model.views.container.Container;

public class ContainerView extends View {
	private Container container;

	public <C extends Container> C container() {
		return (C) container;
	}

	public ContainerView container(Container container) {
		this.container = container;
		return this;
	}
}
