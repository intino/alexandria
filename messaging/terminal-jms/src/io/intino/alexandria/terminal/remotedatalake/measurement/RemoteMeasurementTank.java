package io.intino.alexandria.terminal.remotedatalake.measurement;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.intino.alexandria.Scale;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.measurement.MeasurementEvent;
import io.intino.alexandria.terminal.remotedatalake.DatalakeAccessor;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RemoteMeasurementTank implements Datalake.Store.Tank<MeasurementEvent> {
	private final DatalakeAccessor accessor;
	private final JsonObject tank;
	private final JsonArray sources;

	public RemoteMeasurementTank(DatalakeAccessor accessor, JsonObject tank) {
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
	public Datalake.Store.Source<MeasurementEvent> source(String name) {
		return null;
	}

	@Override
	public Stream<Datalake.Store.Source<MeasurementEvent>> sources() {
		return StreamSupport
				.stream(sources.spliterator(), true)
				.map(s -> new RemoteMeasurementSource(accessor, name(), s.getAsJsonObject()));
	}
}