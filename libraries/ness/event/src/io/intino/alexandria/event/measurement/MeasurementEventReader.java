package io.intino.alexandria.event.measurement;

import io.intino.alexandria.event.EventReader;

import java.io.File;

public class MeasurementEventReader implements EventReader<MeasurementEvent> {

	// TODO

	public MeasurementEventReader(File file) {

	}

	@Override
	public void close() throws Exception {

	}

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public MeasurementEvent next() {
		return null;
	}
}
