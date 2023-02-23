package io.intino.alexandria.datalake.aws.trees.onedepth;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.aws.S3;

public class AwsOneDepthDatalake implements Datalake {

    public static final String PREFIX_DELIMITER = "_";
    public static final String AWS_DELIMITER = "/";
    public static final String FILE_NAME = "1";

    private final S3 s3;
    private final String bucketName;


    public AwsOneDepthDatalake(S3 s3, String bucketName) {
        this.s3 = s3;
        this.bucketName = bucketName;
    }

    @Override
    public EventStore eventStore() {
        return new AwsOneDepthEventStore(s3, bucketName);
    }

    @Override
    public EntityStore entityStore() {
        return new AwsOneDepthEntityStore(s3, bucketName);
    }
}
