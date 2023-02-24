package io.intino.alexandria.event.tuple;

import io.intino.alexandria.ztp.Ztp;
import io.intino.alexandria.ztp.ZtpWriter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class TupleEventWriter implements io.intino.alexandria.event.EventWriter<TupleEvent> {

	private final ZtpWriter writer;

	public TupleEventWriter(File file) throws IOException {
		this(file, false);
	}

	public TupleEventWriter(File file, boolean append) throws IOException {
		this(IO.open(file, append));
	}

	public TupleEventWriter(OutputStream destination) throws IOException {
		this.writer = new ZtpWriter(Ztp.compressing(destination));
	}

	@Override
	public void write(TupleEvent event) throws IOException {
		writer.write(event.toTuple());
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}
}
