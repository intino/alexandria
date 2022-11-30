package io.intino.alexandria.terminal.remotedatalake;

import com.google.gson.JsonObject;
import io.intino.alexandria.Json;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.terminal.JmsConnector;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RemoteDatalake implements Datalake {

	private final DatalakeAccessor accessor;

	public RemoteDatalake(JmsConnector connector) {
		accessor = new DatalakeAccessor(connector);
	}

	@Override
	public EventStore eventStore() {
		return new EventStore() {
			@Override
			public Stream<Tank> tanks() {
				String query = accessor.query("eventstore/tanks");
				if (query == null) return Stream.empty();
				JsonObject content = Json.fromString(query, JsonObject.class);
				return StreamSupport.stream(content.getAsJsonArray().spliterator(), false).map(o -> new RemoteTank(accessor, o.getAsJsonObject()));
			}

			@Override
			public Tank tank(String name) {
				return tanks().filter(t -> t.name().equals(name)).findFirst().orElse(null);
			}
		};
	}


	@Override
	public TripletStore tripletsStore() {
		//TODO
		throw new UnsupportedOperationException();
	}
}
