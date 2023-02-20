package io.intino.alexandria.datalake.file.message;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.message.MessageEvent;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import static io.intino.alexandria.event.Event.Format.Message;

public class MessageEventTub implements Datalake.Store.Tub<MessageEvent> {
	private final File zim;

	public MessageEventTub(File zim) {
		this.zim = zim;
	}

	public String name() {
		return zim.getName().replace(Message.extension(), "");
	}

	@Override
	public Timetag timetag() {
		return new Timetag(name());
	}

	@Override
	public Stream<MessageEvent> events() {
		try {
			return EventStream.of(zim);
		} catch (IOException e) {
			Logger.error(e);
			return Stream.empty();
		}
	}

	public File file() {
		return zim;
	}
}