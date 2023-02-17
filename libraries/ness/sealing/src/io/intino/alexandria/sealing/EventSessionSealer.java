package io.intino.alexandria.sealing;

import io.intino.alexandria.Fingerprint;
import io.intino.alexandria.Session;
import io.intino.alexandria.Session.Type;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.datalake.file.FileStore;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.EventWriter;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
		return FS.allFilesIn(stage, f -> f.getName().endsWith(Session.SessionExtension) && f.length() > 0f);
	}

	private static Fingerprint fingerprintOf(File file) {
		return new Fingerprint(cleanedNameOf(file));
	}

	private static String cleanedNameOf(File file) {
		return file.getName().substring(0, file.getName().indexOf("#")).replace("-", "/").replace(Session.SessionExtension, "");
	}

	private static class EventSealer {
		private final Map<Type, Datalake.Store<? extends Event>> stores;
		private final Predicate<String> sortingPolicy;
		private final File tempFolder;

		EventSealer(Datalake datalake, Predicate<String> sortingPolicy, File tempFolder) {
			this.stores = Map.of(Type.event, datalake.messageStore(), Type.tuple, datalake.tupleStore(), Type.measurement, datalake.measurementStore());
			this.sortingPolicy = sortingPolicy;
			this.tempFolder = tempFolder;
		}

		public void seal(Fingerprint fingerprint, List<File> sessions) throws IOException {
			seal(datalakeFile(fingerprint), sort(fingerprint, sessions));
		}

		private void seal(File datalakeFile, List<File> sessions) throws IOException {
			EventWriter.of(datalakeFile).put(streamOf(sessions));
		}

		private List<File> sort(Fingerprint fingerprint, List<File> files) {
			try {
				for (File file : files)
					if (fingerprint.type().equals(Type.event) && sortingPolicy.test(fingerprint.tank()))
						new MessageEventSorter(file, tempFolder).sort();
				return files;
			} catch (IOException e) {
				Logger.error(e);
				return Collections.emptyList();
			}
		}

		private Stream<Event> streamOf(List<File> files) {
			return EventStream.merge(files.stream().map(file -> {
				try {
					return EventStream.of(file);
				} catch (IOException e) {
					Logger.error(e);
					return Stream.empty();
				}
			}));
		}

		private File datalakeFile(Fingerprint fingerprint) {
			FileStore store = (FileStore) stores.get(fingerprint.type());
			File zimFile = new File(store.directory(), fingerprint + store.fileExtension());
			zimFile.getParentFile().mkdirs();
			return zimFile;
		}
	}
}
