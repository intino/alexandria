package io.intino.alexandria.ui.actions;

import io.intino.alexandria.ui.UiFrameworkBox;
import io.intino.alexandria.ui.displays.AlexandriaDesktop;
import io.intino.alexandria.ui.displays.Soul;
import io.intino.alexandria.ui.services.push.UIClient;

public class HomePageAction extends AbstractHomePageAction {
	public UiFrameworkBox box;

	public Soul prepareSoul(UIClient client) {
	    return new Soul(session) {
			@Override
			public void personify() {
				AlexandriaDesktop display = new AlexandriaDesktop(box);
				register(display);
				display.personify();
			}
		};
	}
}