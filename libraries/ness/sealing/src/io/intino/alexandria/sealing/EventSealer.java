package io.intino.alexandria.sealing;

import io.intino.alexandria.Fingerprint;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.FileStore;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.EventReader;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.EventWriter;
import io.intino.alexandria.event.measurement.MeasurementEventReader;
import io.intino.alexandria.event.message.MessageEventReader;
import io.intino.alexandria.event.resource.ResourceEventReader;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.sealing.SessionSealer.TankNameFilter;
import io.intino.alexandria.sealing.sorters.MessageEventSorter;
import io.intino.alexandria.sealing.sorters.ResourceEventSorter;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class EventSealer {
	private final Map<Event.Format, Datalake.Store<? extends Event>> stores;
	private final TankNameFilter tankNameFilter;
	private final File tempFolder;

	public EventSealer(Datalake datalake, TankNameFilter tankNameFilter, File tempFolder) {
		this.stores = Map.of(Event.Format.Message, datalake.messageStore(), Event.Format.Measurement, datalake.measurementStore(), Event.Format.Resource, datalake.resourceStore());
		this.tankNameFilter = tankNameFilter;
		this.tempFolder = tempFolder;
	}

	public void seal(Fingerprint fingerprint, List<File> sessions) throws IOException {
		seal(datalakeFile(fingerprint), fingerprint.format(), sort(fingerprint, sessions));
	}

	@SuppressWarnings("unchecked")
	private void seal(File datalakeFile, Event.Format type, List<File> sessions) throws IOException {
		try (final EventWriter<Event> writer = EventWriter.of(datalakeFile)) {
			writer.write((Stream<Event>) streamOf(type, sessions));
		}
	}

	private List<File> sort(Fingerprint fingerprint, List<File> files) {
		try {
			EventSorter.Factory sorter = sorterFactoryOf(fingerprint.format());
			if(!tankNameFilter.accepts(fingerprint.tank()) || sorter == null) return Collections.emptyList();
			for (File file : files) sorter.of(file, tempFolder).sort();
			return files;
		} catch (IOException e) {
			Logger.error(e);
			return Collections.emptyList();
		}
	}

	public EventSorter.Factory sorterFactoryOf(Event.Format format) throws IOException {
		switch(format) {
			case Message: return MessageEventSorter::new;
//			case Measurement: return new MessageEventSorter(); TODO?
			case Resource: return ResourceEventSorter::new;
		}
		return null;
	}

	private Stream<? extends Event> streamOf(Event.Format type, List<File> files) throws IOException {
		if (files.size() == 1) return new EventStream<>(readerOf(type, files.get(0)));
		return EventStream.merge(files.stream().map(file -> {
			try {
				return new EventStream<>(readerOf(type, files.get(0)));
			} catch (IOException e) {
				Logger.error(e);
				return Stream.empty();
			}
		}));
	}

	private EventReader<? extends Event> readerOf(Event.Format type, File file) throws IOException {
		if (!file.exists()) return new EventReader.Empty<>();
		switch (type) {
			case Message:
				return new MessageEventReader(file);
			case Measurement:
				return new MeasurementEventReader(file);
			case Resource:
				return new ResourceEventReader(file);
		}
		return new EventReader.Empty<>();
	}

	private File datalakeFile(Fingerprint fingerprint) {
		FileStore store = (FileStore) stores.get(fingerprint.format());
		File datalakeFile = new File(store.directory(), filenameOf(fingerprint) + store.fileExtension());
		datalakeFile.getParentFile().mkdirs();
		return datalakeFile;
	}

	private String filenameOf(Fingerprint fp) {
		return fp.tank() + File.separator + fp.source() + File.separator + fp.timetag();
	}
}
