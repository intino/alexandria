package io.intino.alexandria.datalake.file.eventsourcing;

import io.intino.alexandria.event.Event;

public interface EventHandler {
	void handle(Event event);
}
