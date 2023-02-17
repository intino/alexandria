package io.intino.alexandria.datalake.file.measurement;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.measurement.MeasurementEvent;

import java.io.File;

public class MeasurementEventTank implements Datalake.Store.Tank<MeasurementEvent> {
	private final File root;

	MeasurementEventTank(File root) {
		this.root = root;
	}

	@Override
	public String name() {
		return root.getName();
	}

	public Datalake.Store.Source<MeasurementEvent> source(String name) {
		return new MeasurementEventSource(new File(root, name));
	}

	public File root() {
		return root;
	}


}