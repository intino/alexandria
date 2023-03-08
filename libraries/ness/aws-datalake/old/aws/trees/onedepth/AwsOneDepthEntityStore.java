package io.intino.alexandria.datalake.aws.trees.onedepth;


import io.intino.alexandria.datalake.aws.S3;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.Datalake.EntityStore;
import static io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthDatalake.PREFIX_DELIMITER;

public class AwsOneDepthEntityStore implements EntityStore {

    public static final String ENTITY_EXTENSION = ".triples";
    private static final String INITIAL_PREFIX = "entities_";

    private final S3 s3;
    private final String bucketName;

    public AwsOneDepthEntityStore(S3 s3, String bucketName) {
        this.s3 = s3;
        this.bucketName = bucketName;
    }

    @Override
    public Stream<Tank> tanks() {
        return s3.keysIn(bucketName, INITIAL_PREFIX)
                .parallel()
                .map(prefix -> prefix.split(PREFIX_DELIMITER)[1])
                .distinct()
                .collect(Collectors.toList()).stream()
                .map(tank -> new AwsOneDepthEntityTank(s3, bucketName, prefix(tank)));
    }

    @Override
    public Tank tank(String tank) {
        return new AwsOneDepthEntityTank(s3, bucketName, prefix(tank));
    }

    private String prefix(String tank) {
        return INITIAL_PREFIX + tank + PREFIX_DELIMITER;
    }
}
