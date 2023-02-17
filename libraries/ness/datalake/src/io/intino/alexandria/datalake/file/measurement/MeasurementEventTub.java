package io.intino.alexandria.datalake.file.measurement;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.measurement.MeasurementEvent;

import java.io.File;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.file.measurement.MeasurementEventStore.Extension;

public class MeasurementEventTub implements Datalake.Store.Tub<MeasurementEvent> {
	private final File itz;

	public MeasurementEventTub(File itz) {
		this.itz = itz;
	}

	public String name() {
		return itz.getName().replace(Extension, "");
	}

	@Override
	public Timetag timetag() {
		return new Timetag(name());
	}

	@Override
	public Stream<MeasurementEvent> events() {
		return null; //TODO
	}

	public File file() {
		return itz;
	}
}