package io.intino.alexandria.terminal.remotedatalake.message;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.intino.alexandria.Scale;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.message.MessageEvent;
import io.intino.alexandria.terminal.remotedatalake.DatalakeAccessor;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RemoteMessageTank implements Datalake.Store.Tank<MessageEvent> {
	private final DatalakeAccessor accessor;
	private final JsonObject tank;
	private final JsonArray sources;

	public RemoteMessageTank(DatalakeAccessor accessor, JsonObject tank) {
		this.accessor = accessor;
		this.tank = tank;
		this.sources = tank.get("sources").getAsJsonArray();
	}

	@Override
	public String name() {
		return tank.get("name").getAsString();
	}

	@Override
	public Scale scale() {
		return Scale.valueOf(tank.get("scale").getAsString());
	}

	@Override
	public Datalake.Store.Source<MessageEvent> source(String name) {
		return null;
	}

	@Override
	public Stream<Datalake.Store.Source<MessageEvent>> sources() {
		return StreamSupport
				.stream(sources.spliterator(), true)
				.map(s -> new RemoteMessageSource(accessor, name(), s.getAsJsonObject()));
	}
}