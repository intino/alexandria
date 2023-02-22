package io.intino.alexandria.datalake.aws.trees.tank;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.aws.S3;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.Datalake.EventStore.Tank;
import static io.intino.alexandria.datalake.Datalake.EventStore.Tub;
import static io.intino.alexandria.datalake.aws.trees.tank.AwsDatalake.BucketDelimiter;

public class AwsEventTank implements Tank {
    private final S3 s3;
    private final String bucketName;

    public AwsEventTank(S3 s3, String bucketName) {
        this.s3 = s3;
        this.bucketName = bucketName;
    }

    @Override
    public String name() {
        return "ps." + toPascalCase(bucketName.substring(6));
    }

    private String toPascalCase(String name) {
        return Stream.of(name.split(BucketDelimiter))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase())
                .collect(Collectors.joining());
    }

    @Override
    public Scale scale() {
        return first().timetag().scale();
    }

    @Override
    public Stream<Tub> tubs() {
        return s3.keysIn(bucketName, "").map(key -> new AwsEventTub(s3, bucketName, key));
    }

    @Override
    public Tub first() {
        return tubs().findFirst().orElse(currentTub());
    }

    @Override
    public Tub last() {
        List<String> keys = s3.keysIn(bucketName, "").collect(Collectors.toList());
        return keys.isEmpty() ? null : new AwsEventTub(s3, bucketName, keys.get(keys.size() - 1));
    }

    @Override
    public Tub on(Timetag tag) {
        return new AwsEventTub(s3, bucketName, keyOf(tag));
    }

    private Tub currentTub() {
        return new AwsEventTub(s3, bucketName, "");
    }

    private String keyOf(Timetag tag) {
        return s3.keysIn(bucketName, "")
                .filter(key -> key.contains(tag.value()))
                .findFirst()
                .orElse("");
    }
}
