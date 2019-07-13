package io.intino.alexandria.datalake.file.eventsourcing;

import io.intino.alexandria.message.Message;

public interface EventHandler {
	void handle(Message message);
}
