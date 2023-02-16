package io.intino.alexandria.datalake.aws.trees.day;

import io.intino.alexandria.datalake.aws.S3;

import java.util.stream.Stream;

import static io.intino.alexandria.datalake.Datalake.EntityStore;

public class AwsDayEntityStore implements EntityStore {
    private final S3 s3;

    private AwsDayEntityStore(S3 s3) {
        this.s3 = s3;
    }

    public static EntityStore with(S3 s3) {
        return new AwsDayEntityStore(s3);
    }

    @Override
    public Stream<Tank> tanks() {
        return null;
    }

    @Override
    public Tank tank(String s) {
        return null;
    }
}
