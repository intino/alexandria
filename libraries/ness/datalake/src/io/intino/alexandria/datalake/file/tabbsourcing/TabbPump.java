package io.intino.alexandria.datalake.file.tabbsourcing;


import io.intino.alexandria.datalake.file.eventsourcing.EventPump;

public interface TabbPump {

	EventPump.Reflow reflow(Reflow.Filter filter);

	interface Reflow {
		void next(int blockSize, PopulationHandler... populationHandlers);

		public interface Filter {
		}
	}

	interface PopulationHandler {

	}

}
