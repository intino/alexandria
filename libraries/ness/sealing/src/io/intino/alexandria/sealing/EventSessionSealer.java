package io.intino.alexandria.sealing;

import io.intino.alexandria.Fingerprint;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
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
		return Fingerprint.of(file);
	}

}
