package io.intino.alexandria.datalake.file;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.Datalake.EventStore.Tub;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.EventStream.Sequence;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.file.FileEventStore.EventExtension;

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
	public Scale scale() {
		return first().timetag().scale();
	}

	@Override
	public Stream<Tub> tubs() {
		return tubFiles().map(FileEventTub::new);
	}

	@Override
	public Tub first() {
		return tubs().findFirst().orElse(currentTub());
	}

	@Override
	public Tub last() {
		List<File> files = tubFiles().collect(Collectors.toList());
		return files.isEmpty() ? null : new FileEventTub(files.get(files.size() - 1));
	}

	@Override
	public Tub on(Timetag tag) {
		return new FileEventTub(new File(root, tag.value() + EventExtension));
	}

	@Override
	public EventStream content() {
		return Sequence.of(tubs().map(Tub::events).toArray(EventStream[]::new));
	}

	@Override
	public EventStream content(Predicate<Timetag> filter) {
		return Sequence.of(tubs().filter(t -> filter.test(t.timetag())).map(Tub::events).toArray(EventStream[]::new));
	}

	public File root() {
		return root;
	}

	private Stream<File> tubFiles() {
		return FS.filesIn(root, pathname -> pathname.getName().endsWith(EventExtension));
	}

	private FileEventTub currentTub() {
		return new FileEventTub(new File(root, new Timetag(LocalDateTime.now(), Scale.Month).toString()));
	}
}