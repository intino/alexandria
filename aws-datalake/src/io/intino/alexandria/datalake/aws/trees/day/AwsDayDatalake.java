package io.intino.alexandria.datalake.aws.trees.day;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.aws.S3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AwsDayDatalake implements Datalake {
    public static final String AwsDelimiter = "/";
    public static final int months = 12;
    public static final String AwsBucketDelimiter = "-";
    private final S3 s3;

    public AwsDayDatalake(S3 s3) {
        this.s3 = s3;
    }

    @Override
    public EventStore eventStore() {
        return AwsDayEventStore.with(s3);
    }

    @Override
    public EntityStore entityStore() {
        return AwsDayEntityStore.with(s3);
    }

    public static List<String> buckets() {
            InputStream inputStream = AwsDayDatalake.class.getClassLoader().getResourceAsStream("buckets.txt");
            return new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))
                    .lines()
                    .collect(Collectors.toList());
    }
}
