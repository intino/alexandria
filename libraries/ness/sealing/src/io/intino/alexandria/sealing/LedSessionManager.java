package io.intino.alexandria.sealing;

import io.intino.alexandria.Fingerprint;
import io.intino.alexandria.Session;
import io.intino.alexandria.datalake.file.FS;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LedSessionManager {
	public static void seal(File stageFolder, File ledgerStoreFolder, File tempFolder) {
		ledSessions(stageFolder)
				.collect(Collectors.groupingBy(LedSessionManager::fingerprintOf)).entrySet()
				.stream().sorted(Comparator.comparing(t -> t.getKey().toString()))
				.parallel().forEach(e -> seal(ledgerStoreFolder, tempFolder, e)
		);
	}

	private static void seal(File eventStoreFolder, File tempFolder, Map.Entry<Fingerprint, List<File>> e) {
//		new LedSealer(eventStoreFolder, tempFolder).seal(e.getKey(), e.getValue());TODO
		e.getValue().forEach(f -> f.renameTo(new File(f.getAbsolutePath() + ".treated")));
	}

	private static Stream<File> ledSessions(File stage) {
		return FS.allFilesIn(stage, f -> f.getName().endsWith(Session.LedSessionExtension) && f.length() > 0f);
	}

	private static Fingerprint fingerprintOf(File file) {
		return new Fingerprint(cleanedNameOf(file));
	}

	private static String cleanedNameOf(File file) {
		return file.getName().substring(0, file.getName().indexOf("#")).replace("-", "/").replace(Session.LedSessionExtension, "");
	}

}
