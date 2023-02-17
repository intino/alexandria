package io.intino.alexandria.datalake.file.tuple;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.datalake.file.FileStore;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.tuple.TupleEvent;

import java.io.File;
import java.util.stream.Stream;

public class TupleEventStore implements Datalake.Store<TupleEvent>, FileStore {
	private final File root;

	public TupleEventStore(File root) {
		this.root = root;
		this.root.mkdirs();
	}

	@Override
	public Stream<Tank<TupleEvent>> tanks() {
		return FS.foldersIn(root).map(TupleEventTank::new);
	}

	@Override
	public TupleEventTank tank(String name) {
		return new TupleEventTank(new File(root, name));
	}


	@Override
	public String fileExtension() {
		return Event.Format.Tuple.extension();
	}

	@Override
	public File directory() {
		return root;
	}
}
