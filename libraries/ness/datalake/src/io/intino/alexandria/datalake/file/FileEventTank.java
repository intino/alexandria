package io.intino.alexandria.datalake.file;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.EventReader;
import io.intino.alexandria.event.EventStream;

import java.io.File;
import java.util.function.Predicate;

public class FileEventTank implements Datalake.EventStore.Tank {
	private final File root;

	FileEventTank(File root) {
		this.root = root;
	}

	@Override
	public String name() {
		return root.getName();
	}

	@Override
	public EventStream content() {
		return EventStream.Sequence.of(eventStreams(t -> true));
	}

	@Override
	public EventStream content(Predicate<Timetag> filter) {
		return EventStream.Sequence.of(eventStreams(filter));
	}

	public File root() {
		return root;
	}

	private EventStream[] eventStreams(Predicate<Timetag> filter) {
		return FS.filesIn(root, f -> f.getName().endsWith(FileEventStore.EventExtension))
				.sorted()
				.filter(f -> filter.test(timetagOf(f)))
				.map(EventReader::new)
				.toArray(EventStream[]::new);
	}

	private Timetag timetagOf(File file) {
		return new Timetag(file.getName().replace(FileEventStore.EventExtension, ""));
	}
}