package io.intino.alexandria.datalake.aws.trees.onedepth;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake.EntityStore.Tank;
import io.intino.alexandria.datalake.Datalake.EntityStore.Tub;
import io.intino.alexandria.datalake.aws.S3;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthDatalake.*;
import static io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthEntityStore.ENTITY_EXTENSION;

public class AwsOneDepthEntityTank implements Tank {

    private final S3 s3;
    private final String bucketName;
    private final String prefix;

    public AwsOneDepthEntityTank(S3 s3, String bucketName, String prefix) {
        this.s3 = s3;
        this.bucketName = bucketName;
        this.prefix = prefix;
    }

    @Override
    public String name() {
        String[] rootArray = prefix.split(PREFIX_DELIMITER);
        return rootArray[rootArray.length - 1];
    }

    @Override
    public Stream<Tub> tubs() {
        return s3.keysIn(bucketName, prefix)
                .map(prefix -> new AwsOneDepthEntityTub(s3, bucketName, prefix));
    }

    @Override
    public Tub first() {
        return tubs().findFirst().orElse(null);
    }

    @Override
    public Tub last() {
        return tubs().reduce((first, second) -> second).orElse(null);
    }

    @Override
    public Tub on(Timetag timetag) {
        return new AwsOneDepthEntityTub(s3, bucketName, tubNameOf(timetag));
    }

    @Override
    public Stream<Tub> tubs(int count) {
        return tubs().limit(count);
    }

    @Override
    public Stream<Tub> tubs(Timetag from, Timetag to) {
        return StreamSupport.stream(from.iterateTo(to).spliterator(), false).map(this::on);
    }

    private String tubNameOf(Timetag timetag) {
        return prefix + timetag + AWS_DELIMITER + FILE_NAME + ENTITY_EXTENSION;
    }
}
