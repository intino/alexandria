package io.intino.alexandria.datalake.file;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;

import java.io.File;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FileLedTank implements Datalake.LedStore.Tank {
	private final File root;

	public FileLedTank(File root) {
		this.root = root;
	}

	@Override
	public String name() {
		return root.getName();
	}

	@Override
	public Datalake.LedStore.Tub first() {
		return tubs().findFirst().orElse(currentTub());
	}

	@Override
	public Datalake.LedStore.Tub last() {
		return FS.foldersIn(root, FS.Sort.Reversed).map(FileLedTub::new).findFirst().orElse(currentTub());
	}

	@Override
	public Stream<Datalake.LedStore.Tub> tubs() {
		return FS.foldersIn(root).map(FileLedTub::new);
	}

	@Override
	public Stream<Datalake.LedStore.Tub> tubs(int count) {
		return FS.foldersIn(root, FS.Sort.Reversed).map(f -> (Datalake.LedStore.Tub) new FileLedTub(f)).limit(count);
	}

	@Override
	public Stream<Datalake.LedStore.Tub> tubs(Timetag from, Timetag to) {
		return StreamSupport.stream(from.iterateTo(to).spliterator(), false).map(this::on);
	}

	public Datalake.LedStore.Tub on(Timetag tag) {
		return new FileLedTub(new File(root, tag.value()));
	}

	public File root() {
		return root;
	}

	private FileLedTub currentTub() {
		return new FileLedTub(new File(root, new Timetag(LocalDateTime.now(), Scale.Month).toString()));
	}
}
