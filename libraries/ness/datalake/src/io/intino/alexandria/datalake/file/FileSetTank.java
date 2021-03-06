package io.intino.alexandria.datalake.file;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.Datalake.SetStore.Tub;

import java.io.File;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FileSetTank implements Datalake.SetStore.Tank {
	private final File root;

	public FileSetTank(File root) {
		this.root = root;
	}

	@Override
	public String name() {
		return root.getName();
	}

	@Override
	public Tub first() {
		return tubs().findFirst().orElse(null);
	}

	@Override
	public Tub last() {
		return FS.foldersIn(root, FS.Sort.Reversed).map(FileSetTub::new).findFirst().orElse(null);
	}

	@Override
	public Stream<Tub> tubs() {
		return FS.foldersIn(root).map(FileSetTub::new);
	}

	@Override
	public Stream<Tub> tubs(int count) {
		return FS.foldersIn(root, FS.Sort.Reversed).map(f -> (Tub) new FileSetTub(f)).limit(count);
	}

	@Override
	public Stream<Tub> tubs(Timetag from, Timetag to) {
		return StreamSupport.stream(from.iterateTo(to).spliterator(), false).map(this::on);
	}

	public Tub on(Timetag tag) {
		return new FileSetTub(new File(root, tag.value()));
	}

	public File root() {
		return root;
	}

}
