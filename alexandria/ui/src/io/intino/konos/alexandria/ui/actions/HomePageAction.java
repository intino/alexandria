package io.intino.konos.alexandria.ui.actions;

import io.intino.konos.alexandria.ui.AlexandriaUiBox;
import io.intino.konos.alexandria.exceptions.*;
import io.intino.konos.alexandria.activity.spark.actions.AlexandriaPageAction;
import java.time.*;
import java.util.*;

import io.intino.konos.alexandria.ui.displays.*;

public class HomePageAction extends AbstractHomePageAction {

	public AlexandriaUiBox box;


	public io.intino.konos.alexandria.activity.displays.Soul prepareSoul(io.intino.konos.alexandria.activity.services.push.ActivityClient client) {
	    return new io.intino.konos.alexandria.activity.displays.Soul(session) {
			@Override
			public void personify() {
				AlexandriaDesktop display = new AlexandriaDesktop(box);
				register(display);
				display.personify();
			}
		};
	}
}