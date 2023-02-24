package io.intino.alexandria.sealing;

import io.intino.alexandria.Fingerprint;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.datalake.file.FileStore;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.Event.Format;
import io.intino.alexandria.event.EventReader;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.EventWriter;
import io.intino.alexandria.event.measurement.MeasurementEventReader;
import io.intino.alexandria.event.message.MessageEventReader;
import io.intino.alexandria.event.tuple.TupleEventReader;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static io.intino.alexandria.Session.SessionExtension;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

public class EventSessionSealer {
	private final Datalake datalake;
	private final File stageDir;
	private final File tmpDir;
	private final File treatedDir;

	public EventSessionSealer(Datalake datalake, File stageDir, File tmpDir, File treatedDir) {
		this.datalake = datalake;
		this.stageDir = stageDir;
		this.tmpDir = tmpDir;
		this.treatedDir = treatedDir;
	}

	public void seal() {
		seal(t -> true);
	}

	public void seal(Predicate<String> mustSortTank) {
		sessions(stageDir).collect(groupingBy(EventSessionSealer::fingerprintOf)).entrySet()
				.stream().sorted(comparing(t -> t.getKey().toString()))
				.parallel()
				.forEach(e -> seal(mustSortTank, e));
	}

	private void seal(Predicate<String> sorting, Map.Entry<Fingerprint, List<File>> e) {
		try {
			new EventSealer(datalake, sorting, tmpDir).seal(e.getKey(), e.getValue());
			moveTreated(e);
		} catch (IOException ex) {
			Logger.error(ex);
		}
	}

	private void moveTreated(Map.Entry<Fingerprint, List<File>> e) {
		e.getValue().forEach(f -> f.renameTo(new File(treatedDir, f.getName() + ".treated")));
	}

	private static Stream<File> sessions(File stage) {
		if (!stage.exists()) return Stream.empty();
		return FS.allFilesIn(stage, f -> f.getName().endsWith(SessionExtension) && f.length() > 0f);
	}

	private static Fingerprint fingerprintOf(File file) {
		return new Fingerprint(cleanedNameOf(file));
	}

	private static String cleanedNameOf(File file) {
		return file.getName()
				.substring(0, file.getName().indexOf("#"))
				.replace("-", "/")
				.replace(SessionExtension, "");
	}

	private static class EventSealer {
		private final Map<Format, Datalake.Store<? extends Event>> stores;
		private final Predicate<String> sortingPolicy;
		private final File tempFolder;

		EventSealer(Datalake datalake, Predicate<String> sortingPolicy, File tempFolder) {
			this.stores = Map.of(Format.Message, datalake.messageStore(), Format.Tuple, datalake.tupleStore(), Format.Measurement, datalake.measurementStore());
			this.sortingPolicy = sortingPolicy;
			this.tempFolder = tempFolder;
		}

		public void seal(Fingerprint fingerprint, List<File> sessions) throws IOException {
			seal(datalakeFile(fingerprint), fingerprint.format(), sort(fingerprint, sessions));
		}

		private void seal(File datalakeFile, Format type, List<File> sessions) throws IOException {
			try (final EventWriter<Event> writer = EventWriter.of(datalakeFile)) {
				writer.write((Stream<Event>) streamOf(type, sessions));
			}
		}

		private List<File> sort(Fingerprint fingerprint, List<File> files) {
			try {
				for (File file : files)
					if (fingerprint.format().equals(Format.Message) && sortingPolicy.test(fingerprint.tank()))
						new MessageEventSorter(file, tempFolder).sort();
				return files;
			} catch (IOException e) {
				Logger.error(e);
				return Collections.emptyList();
			}
		}

		private Stream<? extends Event> streamOf(Format type, List<File> files) throws IOException {
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

		private EventReader<? extends Event> readerOf(Format type, File file) throws IOException {
			if (!file.exists()) return new EventReader.Empty<>();
			switch (type) {
				case Message:
					return new MessageEventReader(file);
				case Tuple:
					return new TupleEventReader(file);
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
}
