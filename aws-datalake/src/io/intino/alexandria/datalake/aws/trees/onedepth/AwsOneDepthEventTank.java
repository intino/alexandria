package io.intino.alexandria.datalake.aws.trees.onedepth;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake.EventStore.Tank;
import io.intino.alexandria.datalake.Datalake.EventStore.Tub;
import io.intino.alexandria.datalake.aws.S3;

import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthDatalake.*;
import static io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthEventStore.EVENT_EXTENSION;

public class AwsOneDepthEventTank implements Tank {

    public final S3 s3;
    public final String bucketName;
    public final String prefix;

    public AwsOneDepthEventTank(S3 s3, String bucketName, String prefix) {
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
    public Scale scale() {
        return first().timetag().scale();
    }

    @Override
    public Stream<Tub> tubs() {
        return s3.keysIn(bucketName, prefix)
                .map(prefix -> new AwsOneDepthEventTub(s3, bucketName, prefix));
    }

    @Override
    public Tub first() {
        return tubs().findFirst().orElse(null);
    }

    @Override
    public Tub last() {
        return tubs().reduce((first, second) -> second).orElse(currentTub());
    }

    @Override
    public Tub on(Timetag timetag) {
        return new AwsOneDepthEventTub(s3, bucketName, tubNameOf(timetag));
    }

    private String tubNameOf(Timetag timetag) {
        return prefix + timetag + AWS_DELIMITER + FILE_NAME + EVENT_EXTENSION;
    }

    private Tub currentTub() {
        return new AwsOneDepthEventTub(s3, bucketName, "");
    }
}
