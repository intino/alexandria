package io.intino.alexandria.datalake.aws.trees.day;

import io.intino.alexandria.datalake.aws.S3;

import java.util.stream.Stream;

import static io.intino.alexandria.datalake.Datalake.*;


public class AwsDayEventStore implements EventStore {
    private final S3 s3;

    public AwsDayEventStore(S3 s3) {
        this.s3 = s3;
    }

    public static EventStore with(S3 s3) {
        return new AwsDayEventStore(s3);
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
