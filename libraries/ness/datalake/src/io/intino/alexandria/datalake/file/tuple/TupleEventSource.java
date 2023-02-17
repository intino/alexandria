package io.intino.alexandria.datalake.file.tuple;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.Datalake.Store.Source;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.event.triplet.TripletEvent;

import java.io.File;
import java.util.stream.Stream;

public class TupleEventSource implements Source<TripletEvent> {
	private final File root;

	public TupleEventSource(File root) {
		this.root = root;
	}

	@Override
	public Scale scale() {
		return first().scale();
	}

	@Override
	public Datalake.Store.Tub<TripletEvent> first() {
		return tubs().findFirst().orElse(null);
	}

	@Override
	public Datalake.Store.Tub<TripletEvent> last() {
		return tubFiles().map(TupleEventTub::new).findFirst().orElse(null);
	}

	@Override
	public Stream<Datalake.Store.Tub<TripletEvent>> tubs() {
		return tubFiles().map(TupleEventTub::new);
	}


	public Datalake.Store.Tub<TripletEvent> on(Timetag tag) {
		return new TupleEventTub(new File(root, tag.value()));
	}

	private Stream<File> tubFiles() {
		return FS.filesIn(root, pathname -> pathname.getName().endsWith(TupleEventStore.Extension));
	}

}
