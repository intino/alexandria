package io.intino.konos.alexandria.activity.model.panel;

import io.intino.konos.alexandria.activity.model.AbstractView;
import io.intino.konos.alexandria.activity.model.ElementRender;

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
