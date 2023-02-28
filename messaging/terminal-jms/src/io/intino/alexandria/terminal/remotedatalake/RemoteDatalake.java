package io.intino.alexandria.terminal.remotedatalake;

import com.google.gson.JsonArray;
import io.intino.alexandria.Json;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.measurement.MeasurementEvent;
import io.intino.alexandria.event.message.MessageEvent;
import io.intino.alexandria.jms.MessageReader;
import io.intino.alexandria.terminal.JmsConnector;
import io.intino.alexandria.terminal.remotedatalake.measurement.RemoteMeasurementTank;
import io.intino.alexandria.terminal.remotedatalake.message.RemoteMessageTank;

import javax.jms.Message;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RemoteDatalake implements Datalake {

	private final DatalakeAccessor accessor;

	public RemoteDatalake(JmsConnector connector) {
		accessor = new DatalakeAccessor(connector);
	}

	@Override
	public Store<MessageEvent> messageStore() {
		return new Store<>() {
			@Override
			public Stream<Tank<MessageEvent>> tanks() {
				Message response = accessor.query("eventStore/tanks");
				if (response == null) return Stream.empty();
				JsonArray content = Json.fromString(MessageReader.textFrom(response), JsonArray.class);
				return StreamSupport.stream(content.spliterator(), false).map(o -> new RemoteMessageTank(accessor, o.getAsJsonObject()));
			}

			@Override
			public Tank<MessageEvent> tank(String name) {
				return tanks().filter(t -> t.name().equals(name)).findFirst().orElse(null);
			}
		};
	}

	@Override
	public Store<MeasurementEvent> measurementStore() {
		return new Store<>() {
			@Override
			public Stream<Tank<MeasurementEvent>> tanks() {
				Message response = accessor.query("eventStore/tanks");
				if (response == null) return Stream.empty();
				JsonArray content = Json.fromString(MessageReader.textFrom(response), JsonArray.class);
				return StreamSupport.stream(content.spliterator(), false).map(o -> new RemoteMeasurementTank(accessor, o.getAsJsonObject()));
			}

			@Override
			public Tank<MeasurementEvent> tank(String name) {
				return tanks().filter(t -> t.name().equals(name)).findFirst().orElse(null);
			}
		};
	}
}
