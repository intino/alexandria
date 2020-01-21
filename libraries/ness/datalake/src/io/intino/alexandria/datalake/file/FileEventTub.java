package io.intino.alexandria.datalake.file;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.EventReader;
import io.intino.alexandria.event.EventStream;

import java.io.File;

public class FileEventTub implements Datalake.EventStore.Tub {
	private final File zim;

	public FileEventTub(File zim) {
		this.zim = zim;
	}

	public String name() {
		return zim.getName().replace(FileEventStore.EventExtension, "");
	}

	@Override
	public Timetag timetag() {
		return new Timetag(name());
	}

	@Override
	public EventStream events() {
		return new EventReader(zim);
	}

	public File file() {
		return zim;
	}
}