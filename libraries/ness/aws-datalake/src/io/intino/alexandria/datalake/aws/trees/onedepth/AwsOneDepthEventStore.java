package io.intino.alexandria.datalake.aws.trees.onedepth;

import io.intino.alexandria.datalake.Datalake.EventStore;
import io.intino.alexandria.datalake.aws.S3;

import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthDatalake.PREFIX_DELIMITER;

public class AwsOneDepthEventStore implements EventStore {

    public static final String EVENT_EXTENSION = ".zim";
    private static final String INITIAL_PREFIX = "events_";

    private final S3 s3;
    private final String bucketName;

    public AwsOneDepthEventStore(S3 s3, String bucketName) {
        this.s3 = s3;
        this.bucketName = bucketName;
    }

    @Override
    public Stream<Tank> tanks() {
        return s3.keysIn(bucketName, INITIAL_PREFIX)
                .map(prefix -> prefix.split(PREFIX_DELIMITER)[1])
                .distinct()
                .map(tank -> new AwsOneDepthEventTank(s3, bucketName, prefix(tank)));
    }

    @Override
    public Tank tank(String tank) {
        return new AwsOneDepthEventTank(s3, bucketName, prefix(tank));
    }

    private String prefix(String tank) {
        return INITIAL_PREFIX + tank + PREFIX_DELIMITER;
    }
}
