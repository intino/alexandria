package io.intino.alexandria.sealing;

import io.intino.alexandria.Fingerprint;
import io.intino.alexandria.Session;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.datalake.file.FileEventStore;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

public class LedSessionManager {
	public static void seal(File stageFolder, File ledgerStoreFolder) {
		ledSessions(stageFolder).sorted(comparing(File::getName))
				.parallel().forEach(e -> sealLed(ledgerStoreFolder, e));
	}

	private static void sealLed(File eventStoreFolder, File session) {
		try {
			File file = datalakeFile(eventStoreFolder, fingerprintOf(session));
			FS.copyInto(file, new FileInputStream(session));
			session.renameTo(new File(session.getAbsolutePath() + ".treated"));
		} catch (FileNotFoundException e) {
			Logger.error(e);
		}
	}

	private static Stream<File> ledSessions(File stage) {
		return FS.allFilesIn(stage, f -> f.getName().endsWith(Session.LedSessionExtension) && f.length() > 0f);
	}

	private static File datalakeFile(File eventStoreFolder, Fingerprint fingerprint) {
		File zimFile = new File(eventStoreFolder, fingerprint.toString() + FileEventStore.EventExtension);
		zimFile.getParentFile().mkdirs();
		return zimFile;
	}

	private static Fingerprint fingerprintOf(File file) {
		return new Fingerprint(cleanedNameOf(file));
	}

	private static String cleanedNameOf(File file) {
		return file.getName().substring(0, file.getName().indexOf("#")).replace("-", "/").replace(Session.LedSessionExtension, "");
	}

}
