package io.intino.alexandria.ui.displays.events.eventline;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.model.eventline.EventlineDatasource;

public class SelectEvent extends Event {
	private final EventlineDatasource.Event event;

	public SelectEvent(Display sender, EventlineDatasource.Event event) {
		super(sender);
		this.event = event;
	}

	public EventlineDatasource.Event event() {
		return event;
	}

}
