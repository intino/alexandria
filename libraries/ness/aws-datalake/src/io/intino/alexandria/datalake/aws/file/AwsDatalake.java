package io.intino.alexandria.datalake.aws.file;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.aws.S3;

public class AwsDatalake implements Datalake {
    public static final String AwsDelimiter = "/";
    private final String root = "datalake" + AwsDelimiter;
    public S3 s3;
    public String bucketName;

    public AwsDatalake(S3 s3, String bucketName) {
        this.s3 = s3;
        this.bucketName = bucketName;
    }

    @Override
    public EventStore eventStore() {
        return new AwsEventStore(s3, bucketName, eventFolder());
    }

    @Override
    public EntityStore entityStore() {
        return new AwsEntityStore(s3,bucketName, entityFolder());
    }

    private String eventFolder() {
        return root + EventStoreFolder + AwsDelimiter;
    }

    private String entityFolder() {
        return root + TripletStoreFolder + AwsDelimiter;
    }
}
