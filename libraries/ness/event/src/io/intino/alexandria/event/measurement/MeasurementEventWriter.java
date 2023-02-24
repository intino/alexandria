package io.intino.alexandria.event.measurement;

import io.intino.alexandria.event.EventWriter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class MeasurementEventWriter implements EventWriter<MeasurementEvent> {

	// TODO

	public MeasurementEventWriter(File file) throws IOException {
		this(file, false);
	}

	public MeasurementEventWriter(File file, boolean append) throws IOException {
		this(IO.open(file, append));
	}

	public MeasurementEventWriter(OutputStream destination) throws IOException {
	}

	@Override
	public void write(MeasurementEvent event) throws IOException {

	}

	@Override
	public void flush() throws IOException {

	}

	@Override
	public void close() throws IOException {

	}
}
