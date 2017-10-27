package io.intino.konos.server.activity.displays.catalogs.navigators;

import io.intino.konos.exceptions.KonosException;
import io.intino.konos.server.activity.displays.DisplayNotifierProvider;
import io.intino.konos.server.activity.spark.ActivitySparkManager;
import io.intino.konos.server.activity.spark.resources.DisplayRequester;

public class TimeNavigatorDisplayRequester extends DisplayRequester {

	public TimeNavigatorDisplayRequester(ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws KonosException {
		TimeNavigatorDisplay display = display();
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