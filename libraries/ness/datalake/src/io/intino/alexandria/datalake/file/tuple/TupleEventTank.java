package io.intino.alexandria.datalake.file.tuple;

import io.intino.alexandria.datalake.Datalake.Store.Source;
import io.intino.alexandria.datalake.Datalake.Store.Tank;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.event.tuple.TupleEvent;

import java.io.File;
import java.util.stream.Stream;

public class TupleEventTank implements Tank<TupleEvent> {
	private final File root;

	public TupleEventTank(File root) {
		this.root = root;
	}

	@Override
	public String name() {
		return root.getName();
	}

	@Override
	public Source<TupleEvent> source(String name) {
		return new TupleEventSource(new File(root, name));
	}

	@Override
	public Stream<Source<TupleEvent>> sources() {
		return FS.foldersIn(root).map(TupleEventSource::new);
	}

	public File root() {
		return root;
	}
}