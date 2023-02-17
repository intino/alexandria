package io.intino.alexandria.datalake.file.message;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.Datalake.Store.Tub;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.event.message.MessageEvent;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.alexandria.event.Event.Format.Message;

public class MessageEventSource implements Datalake.Store.Source<MessageEvent> {
	private final File root;

	public MessageEventSource(File file) {
		this.root = file;
	}

	@Override
	public Scale scale() {
		return first().scale();
	}

	@Override
	public Tub<MessageEvent> first() {
		return tubs().findFirst().orElse(null);
	}

	@Override
	public Tub<MessageEvent> last() {
		List<File> files = tubFiles().collect(Collectors.toList());
		return files.isEmpty() ? null : new MessageEventTub(files.get(files.size() - 1));
	}

	@Override
	public Stream<Tub<MessageEvent>> tubs() {
		return tubFiles().map(MessageEventTub::new);
	}

	@Override
	public Tub<MessageEvent> on(Timetag tag) {
		return new MessageEventTub(new File(root, tag.value() + Message.extension()));
	}

	private Stream<File> tubFiles() {
		return FS.filesIn(root, pathname -> pathname.getName().endsWith(Message.extension()));
	}
}
