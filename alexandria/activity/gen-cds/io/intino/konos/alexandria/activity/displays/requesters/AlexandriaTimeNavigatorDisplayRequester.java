package io.intino.konos.alexandria.activity.displays.requesters;

import io.intino.konos.alexandria.activity.displays.AlexandriaTimeNavigatorDisplay;

import io.intino.konos.alexandria.exceptions.AlexandriaException;
import io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifierProvider;
import io.intino.konos.alexandria.activity.spark.ActivitySparkManager;
import io.intino.konos.alexandria.activity.spark.resources.DisplayRequester;

public class AlexandriaTimeNavigatorDisplayRequester extends DisplayRequester {

	public AlexandriaTimeNavigatorDisplayRequester(ActivitySparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws AlexandriaException {
		AlexandriaTimeNavigatorDisplay display = display();
		if (display == null) return;
		String operation = operation();

		if (operation.equals("selectScale")) display.selectScale(manager.fromQuery("value", String.class));
		else if (operation.equals("selectDate")) display.selectDate(manager.fromQuery("value", java.time.Instant.class));
		else if (operation.equals("previousDate")) display.previousDate();
		else if (operation.equals("nextDate")) display.nextDate();
		else if (operation.equals("play")) display.play();
		else if (operation.equals("pause")) display.pause();
	}
}