package io.intino.alexandria.datalake.file.tabbsourcing;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.eventsourcing.EventHandler;
import io.intino.alexandria.datalake.file.eventsourcing.EventPump;

public class FileTabbPump implements TabbPump {
	private final Datalake.SetStore store;

	public FileTabbPump(Datalake.SetStore store) {
		this.store = store;
	}


	@Override
	public EventPump.Reflow reflow(Reflow.Filter filter) {
		return new EventPump.Reflow() {
			@Override
			public void next(int blockSize, EventHandler... eventHandlers) {

			}

			@Override
			public boolean hasNext() {
				return false;
			}
		};
	}
}
