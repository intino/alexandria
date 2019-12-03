package io.intino.alexandria.sealing;

import io.intino.alexandria.Fingerprint;
import io.intino.alexandria.Session;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.datalake.file.FileEventStore;
import io.intino.alexandria.event.EventReader;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.EventWriter;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventSessionManager {

	public static void seal(File stageFolder, File eventStoreFolder, File tempFolder) {
		eventSessions(stageFolder)
				.collect(Collectors.groupingBy(EventSessionManager::fingerprintOf)).entrySet()
				.stream().sorted(Comparator.comparing(t -> t.getKey().toString()))
				.parallel().forEach(e -> new Sealer(eventStoreFolder, tempFolder).seal(e.getKey(), e.getValue()));
	}

	private static Stream<File> eventSessions(File stage) {
		return FS.allFilesIn(stage, f -> f.getName().endsWith(Session.EventSessionExtension) && f.length() > 0f);
	}

	private static EventReader reader(File zimFile) {
		return new EventReader(zimFile);
	}

	private static Fingerprint fingerprintOf(File file) {
		return new Fingerprint(cleanedNameOf(file));
	}

	private static String cleanedNameOf(File file) {
		return file.getName().substring(0, file.getName().indexOf("#")).replace("-", "/").replace(Session.EventSessionExtension, "");
	}

	private static class Sealer {
		private final File eventStoreFolder;
		private final File tempFolder;

		Sealer(File eventStoreFolder, File tempFolder) {
			this.eventStoreFolder = eventStoreFolder;
			this.tempFolder = tempFolder;
		}

		public void seal(Fingerprint fingerprint, List<File> files) {
			seal(datalakeFile(fingerprint), sort(files));
		}

		private void seal(File datalakeFile, List<File> files) {
			new EventWriter(datalakeFile).put(eventStreamOf(files));
		}

		private List<File> sort(List<File> files) {
			try {
				for (File file : files) new EventSorter(file, tempFolder).sort();
				return files;
			} catch (IOException e) {
				Logger.error(e);
				return Collections.emptyList();
			}
		}

		private EventStream.Merge eventStreamOf(List<File> files) {
			return EventStream.Merge.of(files.stream().map(EventSessionManager::reader).toArray(EventStream[]::new));
		}

		private File datalakeFile(Fingerprint fingerprint) {
			File zimFile = new File(eventStoreFolder, fingerprint.toString() + FileEventStore.EventExtension);
			zimFile.getParentFile().mkdirs();
			return zimFile;
		}
	}
}
