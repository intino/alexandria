package io.intino.alexandria.datalake.file.measurement;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.datalake.file.FileStore;
import io.intino.alexandria.event.measurement.MeasurementEvent;

import java.io.File;
import java.util.stream.Stream;

import static io.intino.alexandria.event.Event.Format.Measurement;

public class MeasurementEventStore implements Datalake.Store<MeasurementEvent>, FileStore {
	private final File root;

	public MeasurementEventStore(File root) {
		this.root = root;
	}

	@Override
	public Stream<Tank<MeasurementEvent>> tanks() {
		return FS.foldersIn(root).map(MeasurementEventTank::new);
	}

	public File directory() {
		return root;
	}

	@Override
	public MeasurementEventTank tank(String name) {
		return new MeasurementEventTank(new File(root, name));
	}

	@Override
	public String fileExtension() {
		return Measurement.extension();
	}
}