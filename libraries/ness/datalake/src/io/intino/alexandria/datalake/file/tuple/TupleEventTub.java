package io.intino.alexandria.datalake.file.tuple;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.FileTub;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.tuple.TupleEvent;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

public class TupleEventTub implements Datalake.Store.Tub<TupleEvent>, FileTub {
	private final File ztp;

	public TupleEventTub(File ztp) {
		this.ztp = ztp;
	}

	public String name() {
		return ztp.getName().replace(Event.Format.Tuple.extension(), "");
	}

	@Override
	public Timetag timetag() {
		return new Timetag(name());
	}

	@Override
	public Stream<TupleEvent> events() {
		try {
			return EventStream.of(ztp);
		} catch(IOException e) {
			Logger.error(e);
			return Stream.empty();
		}
	}

	@Override
	public String fileExtension() {
		return Event.Format.Tuple.extension();
	}

	public File file() {
		return ztp;
	}
}
