package io.intino.alexandria.ui.pages;

import io.intino.alexandria.ui.displays.templates.WidgetTypeTemplate;
import io.intino.alexandria.ui.documentation.Model;

public class MicroSitePage extends AbstractMicroSitePage {
	public String page;

	public io.intino.alexandria.ui.Soul prepareSoul(io.intino.alexandria.ui.services.push.UIClient client) {
	    return new io.intino.alexandria.ui.Soul(session) {
			@Override
			public void personify() {
				WidgetTypeTemplate component = new WidgetTypeTemplate(box);
				component.type(Model.WidgetType.MicroSite.name().toLowerCase());
				register(component);
				component.init();
				component.widget().microSiteExamples.microSiteExamplesMold.page(page);
			}
		};
	}
}