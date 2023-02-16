package io.intino.alexandria.datalake.aws;

import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.AwsDatalake.AwsDelimiter;
import static io.intino.alexandria.datalake.aws.AwsDatalake.EventStore;

public class AwsEventStore implements EventStore {
    private final S3 s3;
    private final String bucketName;
    private final String prefix;

    public AwsEventStore(S3 s3, String bucketName, String prefix) {
        this.s3 = s3;
        this.bucketName = bucketName;
        this.prefix = prefix;
    }

    @Override
    public Stream<Tank> tanks() {
        return s3.prefixesIn(bucketName, prefix).map(prefix -> new AwsEventTank(s3, bucketName, prefix));
    }

    public String prefix() {
        return prefix;
    }

    @Override
    public Tank tank(String name) {
        return new AwsEventTank(s3, bucketName, prefix(name));
    }

    private String prefix(String name) {
        return prefix + name + AwsDelimiter;
    }

    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(AwsEventStore.class)) return false;
        AwsEventStore store = (AwsEventStore) obj;
        return this.bucketName.equals(store.bucketName) && this.prefix.equals(store.prefix);
    }
}
