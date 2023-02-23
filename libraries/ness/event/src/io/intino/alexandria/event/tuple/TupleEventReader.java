package io.intino.alexandria.event.tuple;

import io.intino.alexandria.event.EventReader;
import io.intino.alexandria.ztp.TupleReader;
import io.intino.alexandria.ztp.Ztp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TupleEventReader implements EventReader<TupleEvent> {

	private final TupleReader reader;

	public TupleEventReader(File file) throws IOException {
		this.reader = new TupleReader(Ztp.decompressing(new BufferedInputStream(new FileInputStream(file))));
	}

	@Override
	public boolean hasNext() {
		return reader.hasNext();
	}

	@Override
	public TupleEvent next() {
		return new TupleEvent(reader.next());
	}

	@Override
	public void close() throws Exception {
		reader.close();
	}
}
