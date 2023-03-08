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
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class EventSealer {
	private final Map<Event.Format, Datalake.Store<? extends Event>> stores;
	private final Predicate<String> sortingPolicy;
	private final File tempFolder;

	public EventSealer(Datalake datalake, Predicate<String> sortingPolicy, File tempFolder) {
		this.stores = Map.of(Event.Format.Message, datalake.messageStore(), Event.Format.Measurement, datalake.measurementStore());
		this.sortingPolicy = sortingPolicy;
		this.tempFolder = tempFolder;
	}

	public void seal(Fingerprint fingerprint, List<File> sessions) throws IOException {
		seal(datalakeFile(fingerprint), fingerprint.format(), sort(fingerprint, sessions));
	}

	private void seal(File datalakeFile, Event.Format type, List<File> sessions) throws IOException {
		try (final EventWriter<Event> writer = EventWriter.of(datalakeFile)) {
			writer.write((Stream<Event>) streamOf(type, sessions));
		}
	}

	private List<File> sort(Fingerprint fingerprint, List<File> files) {
		try {
			for (File file : files)
				if (fingerprint.format().equals(Event.Format.Message) && sortingPolicy.test(fingerprint.tank()))
					new MessageEventSorter(file, tempFolder).sort();
			return files;
		} catch (IOException e) {
			Logger.error(e);
			return Collections.emptyList();
		}
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
		}
		return new EventReader.Empty<>();
	}

	private File datalakeFile(Fingerprint fingerprint) {
		FileStore store = (FileStore) stores.get(fingerprint.format());
		File zimFile = new File(store.directory(), fingerprint.tank() + File.separator + fingerprint.source() + File.separator + fingerprint.timetag() + store.fileExtension());
		zimFile.getParentFile().mkdirs();
		return zimFile;
	}
}
