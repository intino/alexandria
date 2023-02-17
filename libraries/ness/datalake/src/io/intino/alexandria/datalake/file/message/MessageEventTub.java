package io.intino.alexandria.datalake.file.message;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.message.EventReader;
import io.intino.alexandria.event.message.MessageEvent;

import java.io.File;
import java.util.stream.Stream;

public class MessageEventTub implements Datalake.Store.Tub<MessageEvent> {
	private final File zim;

	public MessageEventTub(File zim) {
		this.zim = zim;
	}

	public String name() {
		return zim.getName().replace(MessageEventStore.EventExtension, "");
	}

	@Override
	public Timetag timetag() {
		return new Timetag(name());
	}

	@Override
	public Stream<MessageEvent> events() {
		return new EventReader(zim); //TODO
	}

	public File file() {
		return zim;
	}
}