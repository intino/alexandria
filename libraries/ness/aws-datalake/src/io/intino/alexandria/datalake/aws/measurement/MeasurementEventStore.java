package io.intino.alexandria.datalake.aws.measurement;

import io.intino.alexandria.datalake.Datalake.Store;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.event.measurement.MeasurementEvent;

import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.AwsDatalake.PrefixDelimiter;

public class MeasurementEventStore implements Store<MeasurementEvent> {
    public static final String MeasurementPrefix = "measurement";
    private final S3 s3;
    private final String bucketName;

    public MeasurementEventStore(S3 s3, String bucketName) {
        this.s3 = s3;
        this.bucketName = bucketName;
    }

    @Override
    public Stream<Tank<MeasurementEvent>> tanks() {
        return s3.keysIn(bucketName, MeasurementPrefix)
                .map(prefix -> prefix.substring(from(prefix), to(prefix)))
                .distinct()
                .map(tank -> new MeasurementEventTank(s3, bucketName, prefixOf(tank)));
    }

    @Override
    public Tank<MeasurementEvent> tank(String name) {
        return new MeasurementEventTank(s3, bucketName, prefixOf(name));
    }

    private int from(String s) {
        return s.indexOf(PrefixDelimiter) + 1;
    }

    private int to(String s) {
        return s.indexOf(PrefixDelimiter, from(s));
    }

    private String prefixOf(String name) {
        return MeasurementPrefix + PrefixDelimiter + name;
    }
}
