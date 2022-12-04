package io.intino.alexandria.terminal.remotedatalake;

import com.google.gson.JsonArray;
import io.intino.alexandria.Json;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.jms.MessageReader;
import io.intino.alexandria.terminal.JmsConnector;

import javax.jms.Message;
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
				Message response = accessor.query("eventStore/tanks");
				if (response == null) return Stream.empty();
				JsonArray content = Json.fromString(MessageReader.textFrom(response), JsonArray.class);
				return StreamSupport.stream(content.spliterator(), false).map(o -> new RemoteTank(accessor, o.getAsJsonObject()));
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
