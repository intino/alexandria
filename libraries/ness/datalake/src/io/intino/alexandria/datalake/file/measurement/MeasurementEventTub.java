package io.intino.alexandria.datalake.file.measurement;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.measurement.MeasurementEvent;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import static io.intino.alexandria.event.Event.Format.Measurement;


public class MeasurementEventTub implements Datalake.Store.Tub<MeasurementEvent> {
	private final File itz;

	public MeasurementEventTub(File itz) {
		this.itz = itz;
	}

	public String name() {
		return itz.getName().replace(Measurement.extension(), "");
	}

	@Override
	public Timetag timetag() {
		return new Timetag(name());
	}

	@Override
	public Stream<MeasurementEvent> events() {
		try {
			return EventStream.of(itz);
		} catch (IOException e) {
			Logger.error(e);
			return Stream.empty();
		}
	}

	public File file() {
		return itz;
	}
}