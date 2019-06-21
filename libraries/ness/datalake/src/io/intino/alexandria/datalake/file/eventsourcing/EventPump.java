package io.intino.alexandria.datalake.file.eventsourcing;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;

public interface EventPump {
	Reflow reflow(Reflow.Filter filter);

	interface Reflow {
		void next(int blockSize, EventHandler... eventHandlers);

		boolean hasNext();

		interface Filter {
			boolean allow(Datalake.EventStore.Tank tank);

			boolean allow(Datalake.EventStore.Tank tank, Timetag timetag);
		}
	}

	interface ReflowHandler {
		void onBlock(int reflowedMessages);

		void onFinish(int reflowedMessages);
	}

}