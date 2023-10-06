package io.intino.alexandria.ui.displays.events.eventline;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.model.eventline.EventlineDatasource;

import java.util.List;

public class SelectEventListEvent extends Event {
	private final List<EventlineDatasource.Event> events;

	public SelectEventListEvent(Display sender, List<EventlineDatasource.Event> events) {
		super(sender);
		this.events = events;
	}

	public List<EventlineDatasource.Event> events() {
		return events;
	}

}
