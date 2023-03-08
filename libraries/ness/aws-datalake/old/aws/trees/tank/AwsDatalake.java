package io.intino.alexandria.datalake.aws.trees.tank;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.aws.S3;

public class AwsDatalake implements Datalake {
    public static final String KeyDelimiter = "-";
    public static final String BucketDelimiter = "-";
    private final S3 s3;

    public AwsDatalake(S3 s3) {
        this.s3 = s3;
    }

    @Override
    public EventStore eventStore() {
        return new AwsEventStore(s3);
    }

    @Override
    public EntityStore entityStore() {
        return new AwsEntityStore(s3);
    }
}
