package io.intino.alexandria.datalake.file.measurement;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.Datalake.Store.Tub;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.measurement.MeasurementEvent;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MeasurementEventSource implements Datalake.Store.Source<MeasurementEvent> {
	private final File root;

	public MeasurementEventSource(File file) {
		this.root = file;
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
	public Tub<MeasurementEvent> first() {
		return tubs().findFirst().orElse(null);
	}

	@Override
	public Tub<MeasurementEvent> last() {
		List<File> files = tubFiles().collect(Collectors.toList());
		return files.isEmpty() ? null : new MeasurementEventTub(files.get(files.size() - 1));
	}

	@Override
	public Stream<Tub<MeasurementEvent>> tubs() {
		return tubFiles().map(MeasurementEventTub::new);
	}

	@Override
	public Tub<MeasurementEvent> on(Timetag tag) {
		return new MeasurementEventTub(new File(root, tag.value() + Event.Format.Measurement.extension()));
	}

	private Stream<File> tubFiles() {
		return FS.filesIn(root, pathname -> pathname.getName().endsWith(Event.Format.Measurement.extension()));
	}
}