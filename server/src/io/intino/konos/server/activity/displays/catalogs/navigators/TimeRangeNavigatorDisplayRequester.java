package io.intino.konos.server.activity.displays.catalogs.navigators;

import io.intino.konos.exceptions.KonosException;
import io.intino.konos.server.activity.displays.DisplayNotifierProvider;
import io.intino.konos.server.activity.displays.schemas.RequestRange;
import io.intino.konos.server.activity.spark.ActivitySparkManager;
import io.intino.konos.server.activity.spark.resources.DisplayRequester;

public class TimeRangeNavigatorDisplayRequester extends DisplayRequester {

	public TimeRangeNavigatorDisplayRequester(ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	@Override
	public void execute() throws KonosException {
		TimeRangeNavigatorDisplay display = display();
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