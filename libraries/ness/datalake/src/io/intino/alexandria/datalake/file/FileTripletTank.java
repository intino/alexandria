package io.intino.alexandria.datalake.file;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.Datalake.TripletStore.Tub;

import java.io.File;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FileTripletTank implements Datalake.TripletStore.Tank {
	private final File root;

	public FileTripletTank(File root) {
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
		return tubFiles().map(FileTripletTub::new).findFirst().orElse(null);
	}

	@Override
	public Stream<Tub> tubs() {
		return tubFiles().map(FileTripletTub::new);
	}

	@Override
	public Stream<Tub> tubs(int count) {
		return tubFiles().map(f -> (Tub) new FileTripletTub(f)).limit(count);
	}

	private Stream<File> tubFiles() {
		return FS.filesIn(root, pathname -> pathname.getName().endsWith(FileTripletStore.Extension));
	}

	@Override
	public Stream<Tub> tubs(Timetag from, Timetag to) {
		return StreamSupport.stream(from.iterateTo(to).spliterator(), false).map(this::on);
	}

	public Tub on(Timetag tag) {
		return new FileTripletTub(new File(root, tag.value()));
	}

	public File root() {
		return root;
	}

}
