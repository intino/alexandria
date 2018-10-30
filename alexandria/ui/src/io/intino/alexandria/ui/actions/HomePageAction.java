package io.intino.alexandria.ui.actions;

import io.intino.alexandria.ui.displays.AlexandriaDesktop;
import io.intino.alexandria.ui.displays.Soul;
import io.intino.alexandria.ui.services.push.UIClient;
import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.exceptions.*;

import io.intino.alexandria.ui.displays.*;

public class HomePageAction extends AbstractHomePageAction {

	public AlexandriaUiBox box;


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