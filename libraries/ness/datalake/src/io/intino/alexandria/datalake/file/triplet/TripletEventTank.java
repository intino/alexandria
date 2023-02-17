package io.intino.alexandria.datalake.file.triplet;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake.Store.Tank;
import io.intino.alexandria.datalake.Datalake.Store.Tub;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.event.triplet.TripletEvent;

import java.io.File;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TripletEventTank implements Tank<TripletEvent> {
	private final File root;

	public TripletEventTank(File root) {
		this.root = root;
	}

	@Override
	public String name() {
		return root.getName();
	}

	@Override
	public Scale scale() {
		return first().scale();
	}

	@Override
	public Tub<TripletEvent> first() {
		return tubs().findFirst().orElse(null);
	}

	@Override
	public Tub<TripletEvent> last() {
		return tubFiles().map(TripletEventTub::new).findFirst().orElse(null);
	}

	@Override
	public Stream<Tub<TripletEvent>> tubs() {
		return tubFiles().map(TripletEventTub::new);
	}


	private Stream<File> tubFiles() {
		return FS.filesIn(root, pathname -> pathname.getName().endsWith(TripletEventStore.Extension));
	}

	public Tub<TripletEvent> on(Timetag tag) {
		return new TripletEventTub(new File(root, tag.value()));
	}

	@Override
	public Stream<TripletEvent> content() {
		return Tank.super.content();
	}

	@Override
	public Stream<TripletEvent> content(Predicate<Timetag> filter) {
		return Tank.super.content(filter);
	}

	public File root() {
		return root;
	}
}