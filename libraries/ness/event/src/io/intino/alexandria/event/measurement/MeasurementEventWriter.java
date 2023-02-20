package io.intino.alexandria.event.measurement;

import io.intino.alexandria.event.AbstractEventWriter;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

public class MeasurementEventWriter extends AbstractEventWriter<MeasurementEvent> {

	// TODO

	public MeasurementEventWriter(File file) {
		super(file);
	}

	@Override
	protected File merge(Stream<MeasurementEvent> data) throws IOException {
		return null;
	}
}
