package io.intino.alexandria.ui.pages;

import io.intino.alexandria.ui.displays.templates.Main;

public class HomePage extends AbstractHomePage {

	public io.intino.alexandria.ui.Soul prepareSoul(io.intino.alexandria.ui.services.push.UIClient client) {
	    return new io.intino.alexandria.ui.Soul(session) {
			@Override
			public void personify() {
				Main component = new Main(box);
				register(component);
				component.init();
			}
		};
	}
}