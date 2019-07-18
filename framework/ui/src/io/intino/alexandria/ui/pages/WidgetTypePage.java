package io.intino.alexandria.ui.pages;

import io.intino.alexandria.ui.displays.templates.WidgetTypeTemplate;

public class WidgetTypePage extends AbstractWidgetTypePage {
	public String type;

	public io.intino.alexandria.ui.Soul prepareSoul(io.intino.alexandria.ui.services.push.UIClient client) {
	    return new io.intino.alexandria.ui.Soul(session) {
			@Override
			public void personify() {
				WidgetTypeTemplate component = new WidgetTypeTemplate(box);
				component.type(type);
				register(component);
				component.init();
			}
		};
	}
}