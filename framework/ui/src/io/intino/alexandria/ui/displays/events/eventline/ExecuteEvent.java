package io.intino.alexandria.ui.displays.events.eventline;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.model.eventline.EventlineDatasource;

import static io.intino.alexandria.ui.documentation.Model.WidgetType.Eventline;

public class ExecuteEvent extends Event {
	private final EventlineDatasource.Event event;
	private final String operation;

	public ExecuteEvent(Display sender, EventlineDatasource.Event event, String operation) {
		super(sender);
		this.event = event;
		this.operation = operation;
	}

	public EventlineDatasource.Event event() {
		return event;
	}

	public String operation() {
		return operation;
	}
}
