package io.intino.alexandria.datalake.file.eventsourcing;

import io.intino.alexandria.datalake.Datalake;

public interface EventSubscriber {

	Connection connection();

	Subscription subscribe(Datalake.EventStore.Tank tank);

	void unsubscribe(Datalake.EventStore.Tank tank);

	interface Connection {
		void connect(String... args);

		void disconnect();
	}

	interface Subscription {
		default void using(EventHandler... eventHandlers) {
			using(null, eventHandlers);
		}

		void using(String clientId, EventHandler... eventHandlers);
	}
}