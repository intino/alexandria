package io.intino.konos.alexandria.activity.model.panel;

import io.intino.konos.alexandria.activity.model.AbstractView;
import io.intino.konos.alexandria.activity.model.ElementRender;

import static io.intino.konos.alexandria.activity.model.panel.View.Layout.Tab;

public class View extends AbstractView {
	private ElementRender render;
	private Layout layout = Tab;

	public <ER extends ElementRender> ER render() {
		return (ER) render;
	}

	public View render(ElementRender render) {
		this.render = render;
		return this;
	}

	public Layout layout() {
		return layout;
	}

	public View layout(Layout layout) {
		this.layout = layout;
		return this;
	}

	public enum Layout {
		Tab, LeftFixed, RightFixed
	}
}
