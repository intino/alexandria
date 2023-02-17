package io.intino.alexandria.datalake.file.message;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.Datalake.Store.Tub;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.event.message.MessageEvent;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.file.message.MessageEventStore.EventExtension;

public class MessageEventTank implements Datalake.Store.Tank<MessageEvent> {
	private final File root;

	MessageEventTank(File root) {
		this.root = root;
	}

	@Override
	public String name() {
		return root.getName();
	}

	@Override
	public Stream<Tub<MessageEvent>> tubs() {
		return tubFiles().map(MessageEventTub::new);
	}

	@Override
	public Tub first() {
		return tubs().findFirst().orElse(currentTub());
	}

	@Override
	public Tub last() {
		List<File> files = tubFiles().collect(Collectors.toList());
		return files.isEmpty() ? null : new MessageEventTub(files.get(files.size() - 1));
	}

	@Override
	public Tub on(Timetag tag) {
		return new MessageEventTub(new File(root, tag.value() + EventExtension));
	}

	public File root() {
		return root;
	}

	private Stream<File> tubFiles() {
		return FS.filesIn(root, pathname -> pathname.getName().endsWith(EventExtension));
	}

	private MessageEventTub currentTub() {
		return new MessageEventTub(new File(root, new Timetag(LocalDateTime.now(), Scale.Month).toString()));
	}
}