package io.intino.alexandria.datalake.aws.trees.day;

import io.intino.alexandria.datalake.aws.S3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.Datalake.EntityStore;

public class AwsDayEntityStore implements EntityStore {
    public static final String csvDelimiter = ",";
    private final S3 s3;

    private AwsDayEntityStore(S3 s3) {
        this.s3 = s3;
    }

    public static EntityStore with(S3 s3) {
        return new AwsDayEntityStore(s3);
    }

    @Override
    public Stream<Tank> tanks() {
        return new BufferedReader(new InputStreamReader(Objects.requireNonNull(getMetadata())))
                .lines().filter(s -> s.startsWith("entities")).map(s -> new AwsDayEntityTank(s.split(csvDelimiter)[1]));
    }

    @Override
    public Tank tank(String s) {
        return new AwsDayEntityTank(new BufferedReader(new InputStreamReader(Objects.requireNonNull(getMetadata())))
                .lines().filter(l -> l.endsWith(s)).findFirst().get().split(csvDelimiter)[1]);
    }

    private static InputStream getMetadata() {
        return AwsDayDatalake.class.getClassLoader().getResourceAsStream("metadata.csv");
    }
}
