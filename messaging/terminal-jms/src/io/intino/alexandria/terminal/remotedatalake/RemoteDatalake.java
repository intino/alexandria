package io.intino.alexandria.terminal.remotedatalake;

import com.google.gson.JsonArray;
import io.intino.alexandria.Json;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.measurement.MeasurementEvent;
import io.intino.alexandria.event.message.MessageEvent;
import io.intino.alexandria.event.resource.ResourceEvent;
import io.intino.alexandria.jms.MessageReader;
import io.intino.alexandria.terminal.JmsConnector;
import io.intino.alexandria.terminal.remotedatalake.measurement.RemoteMeasurementTank;
import io.intino.alexandria.terminal.remotedatalake.message.RemoteMessageTank;
import io.intino.alexandria.terminal.remotedatalake.resource.RemoteResourceTank;
import jakarta.jms.Message;

import java.util.Optional;
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
				Message response = accessor.query("messageStore/tanks");
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
				Message response = accessor.query("measurementStore/tanks");
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

	@Override
	public ResourceStore resourceStore() {
		return new ResourceStore() {
			@Override
			public Optional<ResourceEvent> find(ResourceEvent.REI rei) {
				return Optional.empty();
			}

			@Override
			public Stream<Tank<ResourceEvent>> tanks() {
				Message response = accessor.query("resourceStore/tanks");
				if (response == null) return Stream.empty();
				JsonArray content = Json.fromString(MessageReader.textFrom(response), JsonArray.class);
				return StreamSupport.stream(content.spliterator(), false).map(o -> new RemoteResourceTank(accessor, o.getAsJsonObject()));
			}

			@Override
			public Tank<ResourceEvent> tank(String name) {
				return tanks().filter(t -> t.name().equals(name)).findFirst().orElse(null);
			}
		};
	}
}
