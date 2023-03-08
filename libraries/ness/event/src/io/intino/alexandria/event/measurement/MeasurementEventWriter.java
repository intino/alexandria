package io.intino.alexandria.event.measurement;

import io.intino.alexandria.event.EventWriter;
import io.intino.alexandria.zit.ZitWriter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class MeasurementEventWriter implements EventWriter<MeasurementEvent> {

	private final ZitWriter writer;

	public MeasurementEventWriter(File file) throws IOException {
		this(file, false);
	}

	public MeasurementEventWriter(File file, boolean append) throws IOException {
		this(IO.open(file, append));
	}

	public MeasurementEventWriter(OutputStream destination) throws IOException {
		this.writer = new ZitWriter(destination);
	}

	@Override
	public void write(MeasurementEvent event) throws IOException {
		writer.put(event.ts(), event.values());
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}
}
