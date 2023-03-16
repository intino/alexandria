package io.intino.alexandria.sealing.aws;

import com.amazonaws.services.s3.AmazonS3;
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

public class AwsEventSessionSealer {

    private final Datalake datalake;
    private final String bucketName;
    private final File stageDir;
    private final File tmpDir;
    private final AmazonS3 client;

    private final File treatedFile;

    public AwsEventSessionSealer(Datalake datalake, String bucketName, File stageDir, File tmpDir, AmazonS3 client, File treatedFile) {
        this.datalake = datalake;
        this.bucketName = bucketName;
        this.stageDir = stageDir;
        this.tmpDir = tmpDir;
        this.client = client;
        this.treatedFile = treatedFile;
    }

    public void seal() {
        seal(t -> true);
    }

    public void seal(Predicate<String> mustSortTank) {
        sessions(stageDir).collect(groupingBy(AwsEventSessionSealer::fingerprintOf)).entrySet()
                .stream().sorted(comparing(t -> t.getKey().toString()))
                .parallel()
                .forEach(e -> seal(mustSortTank, e));
    }

    private void seal(Predicate<String> sorting, Map.Entry<Fingerprint, List<File>> e) {
        try {
            new AwsEventSealer(datalake, bucketName, sorting, tmpDir, client).seal(e.getKey(), e.getValue());
            moveTreated(e);
        } catch (IOException ex) {
            Logger.error(ex);
        }
    }

    private void moveTreated(Map.Entry<Fingerprint, List<File>> e) {
//        e.getValue().forEach(f -> System.out.println((new File(treatedFile, f.getName() + ".treated"))));
    }

    private static Stream<File> sessions(File stage) {
        if (!stage.exists()) return Stream.empty();
        return FS.allFilesIn(stage, f -> f.getName().endsWith(SessionExtension) && f.length() > 0f);
    }

    private static Fingerprint fingerprintOf(File file) {
        return Fingerprint.of(file);
    }
}
