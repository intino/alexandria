package io.intino.alexandria.terminal.remotedatalake;

import com.google.gson.JsonObject;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.EventStream;

public class RemoteTub implements Datalake.EventStore.Tub {
	private final DatalakeAccessor accessor;

	public RemoteTub(DatalakeAccessor accessor, JsonObject tank, JsonObject t) {
		this.accessor = accessor;
	}

	@Override
	public Timetag timetag() {
		return null;
	}

	@Override
	public EventStream events() {
		return null;
	}
}
