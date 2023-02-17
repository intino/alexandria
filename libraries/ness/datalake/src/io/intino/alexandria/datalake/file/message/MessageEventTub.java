package io.intino.alexandria.datalake.file.message;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.message.MessageEvent;

import java.io.File;
import java.util.stream.Stream;

public class MessageEventTub implements Datalake.Store.Tub<MessageEvent> {
	private final File zim;

	public MessageEventTub(File zim) {
		this.zim = zim;
	}

	public String name() {
		return zim.getName().replace(MessageEventStore.Extension, "");
	}

	@Override
	public Timetag timetag() {
		return new Timetag(name());
	}

	@Override
	public Stream<MessageEvent> events() {
		return EventStream.of(zim);
	}

	public File file() {
		return zim;
	}
}