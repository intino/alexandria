package io.intino.konos.alexandria.activity.box.model.panel;

import io.intino.konos.alexandria.activity.box.model.AbstractView;
import io.intino.konos.alexandria.activity.box.model.ElementRender;

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
