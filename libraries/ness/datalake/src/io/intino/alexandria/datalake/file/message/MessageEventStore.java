package io.intino.alexandria.datalake.file.message;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.FileStore;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.message.MessageEvent;
import io.intino.alexandria.FS;

import java.io.File;
import java.util.stream.Stream;

public class MessageEventStore implements Datalake.Store<MessageEvent>, FileStore {
	private final File root;

	public MessageEventStore(File root) {
		this.root = root;
	}

	@Override
	public Stream<Tank<MessageEvent>> tanks() {
		return FS.directoriesIn(root).map(MessageEventTank::new);
	}

	public File directory() {
		return root;
	}

	@Override
	public MessageEventTank tank(String name) {
		return new MessageEventTank(new File(root, name));
	}

	@Override
	public String fileExtension() {
		return Event.Format.Message.extension();
	}
}
