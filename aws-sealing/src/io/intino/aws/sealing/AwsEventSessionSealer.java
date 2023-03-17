package io.intino.aws.sealing;

import io.intino.alexandria.Fingerprint;
import io.intino.alexandria.Session;
import io.intino.alexandria.datalake.aws.AwsDatalake;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

class AwsEventSessionSealer {

    private final AwsDatalake datalake;
    private final File stageDir;
    private final File tmpDir;
    private final File treatedDir;

    AwsEventSessionSealer(AwsDatalake datalake, File stageDir, File treatedDir, File tmpDir) {
        this.datalake = datalake;
        this.stageDir = stageDir;
        this.treatedDir = treatedDir;
        this.tmpDir = tmpDir;
    }

    void seal() {
        seal(t -> true);
    }

    void seal(Predicate<String> mustSortTank) {
        sessions(stageDir).collect(groupingBy(AwsEventSessionSealer::fingerprintOf)).entrySet()
                .stream().sorted(comparing(t -> t.getKey().toString()))
                .parallel()
                .forEach(e -> seal(mustSortTank, e));
    }

    private void seal(Predicate<String> sorting, Map.Entry<Fingerprint, List<File>> e) {
        try {
            new AwsEventSealer(datalake, sorting, tmpDir).seal(e.getKey(), e.getValue());
            moveTreated(e);
        } catch (IOException ex) {
            Logger.error(ex);
        }
        moveTreated(e);
    }

    private void moveTreated(Map.Entry<Fingerprint, List<File>> e) {
        e.getValue().forEach(f -> f.renameTo(new File(treatedDir, f.getName() + ".treated")));
    }

    private static Stream<File> sessions(File stage) {
        if (!stage.exists()) return Stream.empty();
        return FS.allFilesIn(stage, f -> f.getName().endsWith(Session.SessionExtension) && f.length() > 0f);
    }

    private static Fingerprint fingerprintOf(File file) {
        return Fingerprint.of(file);
    }
}
