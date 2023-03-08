package io.intino.alexandria.datalake.aws.file;

import io.intino.alexandria.datalake.Datalake.EntityStore;
import io.intino.alexandria.datalake.aws.S3;

import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.file.AwsDatalake.AwsDelimiter;

public class AwsEntityStore implements EntityStore {
    public static final String Extension = ".triples";
    private final S3 s3;
    private final String bucketName;
    private final String prefix;

    public AwsEntityStore(S3 s3, String bucketName, String prefix) {
        this.s3 = s3;
        this.bucketName = bucketName;
        this.prefix = prefix;
    }

    @Override
    public Stream<Tank> tanks() {
        return s3.prefixesIn(bucketName, prefix)
                .map(prefix -> new AwsEntityTank(s3, bucketName, prefix));
    }

    @Override
    public Tank tank(String name) {
        return new AwsEntityTank(s3, bucketName, prefix + name + AwsDelimiter);
    }

    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(AwsEntityStore.class)) return false;
        AwsEntityStore store = (AwsEntityStore) obj;
        return this.bucketName.equals(store.bucketName) && this.prefix.equals(store.prefix);
    }
}
