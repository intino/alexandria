package io.intino.konos.alexandria.ui.actions;

import io.intino.konos.alexandria.ui.AlexandriaUiBox;
import io.intino.konos.alexandria.exceptions.*;
import io.intino.konos.alexandria.ui.spark.actions.AlexandriaResourceAction;
import java.time.*;
import java.util.*;

import io.intino.konos.alexandria.ui.displays.*;

public class HomePageAction extends AbstractHomePageAction {

	public AlexandriaUiBox box;


	public io.intino.konos.alexandria.ui.displays.Soul prepareSoul(io.intino.konos.alexandria.ui.services.push.UIClient client) {
	    return new io.intino.konos.alexandria.ui.displays.Soul(session) {
			@Override
			public void personify() {
				AlexandriaDesktop display = new AlexandriaDesktop(box);
				register(display);
				display.personify();
			}
		};
	}
}