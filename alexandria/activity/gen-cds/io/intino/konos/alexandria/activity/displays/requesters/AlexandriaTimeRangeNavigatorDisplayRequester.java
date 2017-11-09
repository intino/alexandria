package io.intino.konos.alexandria.activity.displays.requesters;

import io.intino.konos.alexandria.activity.displays.AlexandriaTimeRangeNavigatorDisplay;
import io.intino.konos.alexandria.activity.schemas.*;

import io.intino.konos.alexandria.exceptions.AlexandriaException;
import io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifierProvider;
import io.intino.konos.alexandria.activity.spark.ActivitySparkManager;
import io.intino.konos.alexandria.activity.spark.resources.DisplayRequester;

public class AlexandriaTimeRangeNavigatorDisplayRequester extends DisplayRequester {

	public AlexandriaTimeRangeNavigatorDisplayRequester(ActivitySparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws AlexandriaException {
		AlexandriaTimeRangeNavigatorDisplay display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("selectScale")) display.selectScale(manager.fromQuery("value", String.class));
		else if (operation.equals("selectFrom")) display.selectFrom(manager.fromQuery("value", java.time.Instant.class));
		else if (operation.equals("selectTo")) display.selectTo(manager.fromQuery("value", java.time.Instant.class));
		else if (operation.equals("move")) display.move(manager.fromQuery("value", RequestRange.class));
		else if (operation.equals("moveNext")) display.moveNext();
		else if (operation.equals("movePrevious")) display.movePrevious();
	}
}