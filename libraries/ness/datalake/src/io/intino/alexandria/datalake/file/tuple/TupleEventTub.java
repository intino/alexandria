package io.intino.alexandria.datalake.file.tuple;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.tuple.TupleEvent;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

public class TupleEventTub implements Datalake.Store.Tub<TupleEvent> {
	private final File tsv;

	public TupleEventTub(File tsv) {
		this.tsv = tsv;
	}

	public String name() {
		return tsv.getName().replace(Event.Format.Tuple.extension(), "");
	}

	@Override
	public Timetag timetag() {
		return new Timetag(name());
	}

	@Override
	public Stream<TupleEvent> events() {
		try {
			return EventStream.of(tsv);
		} catch(IOException e) {
			Logger.error(e);
			return Stream.empty();
		}
	}

	public File file() {
		return tsv;
	}
}
