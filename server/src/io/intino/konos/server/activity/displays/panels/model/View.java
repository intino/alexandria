package io.intino.konos.server.activity.displays.panels.model;

import io.intino.konos.server.activity.displays.elements.model.AbstractView;
import io.intino.konos.server.activity.displays.elements.model.ElementRender;

public class View extends AbstractView {
	private ElementRender render;

	public <ER extends ElementRender> ER render() {
		return (ER) render;
	}

	public View render(ElementRender render) {
		this.render = render;
		return this;
	}
}
