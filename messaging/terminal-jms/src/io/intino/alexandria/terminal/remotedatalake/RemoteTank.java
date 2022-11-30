package io.intino.alexandria.terminal.remotedatalake;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.Datalake.EventStore.Tub;
import io.intino.alexandria.event.EventStream;

import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RemoteTank implements Datalake.EventStore.Tank {
	private final DatalakeAccessor accessor;
	private final JsonObject tank;
	private final JsonArray tubs;

	public RemoteTank(DatalakeAccessor accessor, JsonObject tank) {
		this.accessor = accessor;
		this.tank = tank;
		this.tubs = tank.get("tubs").getAsJsonArray();
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
	public Stream<Tub> tubs() {
		return StreamSupport
				.stream(tubs.spliterator(), false)
				.map(t -> new RemoteTub(accessor, tank, t.getAsJsonObject()));
	}

	@Override
	public Tub first() {
		if (!tubs.isEmpty()) return new RemoteTub(accessor, tank, tubs.get(0).getAsJsonObject());
		return null;
	}

	@Override
	public Tub last() {
		return !tubs.isEmpty() ? new RemoteTub(accessor, tank, tubs.get(tubs.size() - 1).getAsJsonObject()) : null;
	}

	@Override
	public Tub on(Timetag timetag) {
		return tubs().filter(t -> t.timetag().equals(timetag)).findFirst().orElse(null);
	}

	@Override
	public EventStream content() {
		return null;
	}

	@Override
	public EventStream content(Predicate<Timetag> predicate) {
		return null;
	}
}
