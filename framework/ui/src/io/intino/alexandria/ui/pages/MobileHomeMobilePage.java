package io.intino.alexandria.ui.pages;

import io.intino.alexandria.ui.displays.templates.MobileDocsTemplate;

public class MobileHomeMobilePage extends AbstractMobileHomeMobilePage {

	public io.intino.alexandria.ui.Soul prepareSoul(io.intino.alexandria.ui.services.push.UIClient client) {
		return new io.intino.alexandria.ui.Soul(session) {
			@Override
			public void personify() {
				MobileDocsTemplate component = new MobileDocsTemplate(box);
				register(component);
				component.init();
			}
		};
	}
}
