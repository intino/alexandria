package io.intino.alexandria.datalake.aws.trees.tank;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.aws.S3;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.Datalake.EntityStore.Tank;
import static io.intino.alexandria.datalake.Datalake.EntityStore.Tub;
import static io.intino.alexandria.datalake.aws.trees.tank.AwsDatalake.BucketDelimiter;
import static java.util.stream.StreamSupport.stream;

public class AwsEntityTank implements Tank {
    private final S3 s3;
    private final String bucketName;

    public AwsEntityTank(S3 s3, String bucketName) {
        this.s3 = s3;
        this.bucketName = bucketName;
    }

    @Override
    public String name() {
        return toPascalCase(bucketName.substring(7));
    }

    @Override
    public Stream<Tub> tubs() {
        return s3.keysIn(bucketName, "").map(key -> new AwsEntityTub(s3, bucketName, key));
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
    public Tub on(Timetag tag) {
        return new AwsEntityTub(s3, bucketName, keyOf(tag));
    }

    @Override
    public Stream<Tub> tubs(int size) {
        return tubs().limit(size);
    }

    @Override
    public Stream<Tub> tubs(Timetag from, Timetag to) {
        return stream(from.iterateTo(to).spliterator(), true).map(this::on);
    }

    private String toPascalCase(String name) {
        return Stream.of(name.split(BucketDelimiter))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase())
                .collect(Collectors.joining());
    }

    private String keyOf(Timetag tag) {
        return s3.keysIn(bucketName, "")
                .filter(key -> key.contains(tag.value()))
                .findFirst()
                .orElse("");
    }
}
