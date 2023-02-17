package io.intino.alexandria.datalake.file;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.Datalake.Store.Tub;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.file.FileEventStore.EventExtension;

public class FileEventTank implements Datalake.Store.Tank {
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