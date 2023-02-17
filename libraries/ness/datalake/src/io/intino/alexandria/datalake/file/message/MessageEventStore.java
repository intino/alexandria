package io.intino.alexandria.datalake.file.message;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.event.message.MessageEvent;

import java.io.File;
import java.util.stream.Stream;

import static io.intino.alexandria.zim.ZimStream.ZimExtension;

public class MessageEventStore implements Datalake.Store<MessageEvent> {
	public static final String Extension = ZimExtension;
	private final File root;

	public MessageEventStore(File root) {
		this.root = root;
	}

	@Override
	public Stream<Tank<MessageEvent>> tanks() {
		return FS.foldersIn(root).map(MessageEventTank::new);
	}

	public File root() {
		return root;
	}

	@Override
	public MessageEventTank tank(String name) {
		return new MessageEventTank(new File(root, name));
	}

	public String extension() {
		return Extension;
	}

}
