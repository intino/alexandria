package io.intino.alexandria.datalake.file.measurement;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.event.measurement.MeasurementEvent;

import java.io.File;
import java.util.stream.Stream;

public class MeasurementEventStore implements Datalake.Store<MeasurementEvent> {
	public static final String Extension = ".itz";

	private final File root;

	public MeasurementEventStore(File root) {
		this.root = root;
	}

	@Override
	public Stream<Tank<MeasurementEvent>> tanks() {
		return FS.foldersIn(root).map(MeasurementEventTank::new);
	}

	public File root() {
		return root;
	}

	@Override
	public MeasurementEventTank tank(String name) {
		return new MeasurementEventTank(new File(root, name));
	}

	public String extension() {
		return Extension;
	}

}
