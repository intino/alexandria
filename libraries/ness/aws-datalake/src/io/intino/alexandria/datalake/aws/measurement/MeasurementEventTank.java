package io.intino.alexandria.datalake.aws.measurement;

import io.intino.alexandria.datalake.Datalake.Store;
import io.intino.alexandria.datalake.Datalake.Store.Tank;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.event.measurement.MeasurementEvent;

import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.AwsDatalake.PrefixDelimiter;
import static io.intino.alexandria.datalake.aws.measurement.MeasurementEventStore.MeasurementsPrefix;


public class MeasurementEventTank implements Tank<MeasurementEvent> {
    public final S3 s3;
    public final String bucketName;
    public final String prefix;

    public MeasurementEventTank(S3 s3, String bucketName, String prefix) {
        this.s3 = s3;
        this.bucketName = bucketName;
        this.prefix = prefix;
    }

    @Override
    public String name() {
        return prefix.substring(indexOfBucketName());
    }

    @Override
    public Store.Source<MeasurementEvent> source(String name) {
        return new MeasurementEventSource(s3, bucketName, prefix + PrefixDelimiter + name);
    }

    @Override
    public Stream<Store.Source<MeasurementEvent>> sources() {
        return s3.keysIn(bucketName, prefix)
                .map(prefix -> prefix.substring(indexOfBucketName(), to(prefix)))
                .distinct()
                .map(prefix -> new MeasurementEventSource(s3, bucketName, prefix));
    }

    private static int indexOfBucketName() {
        return (MeasurementsPrefix + PrefixDelimiter).length() + 1;
    }

    private static int to(String s) {
        return s.indexOf(PrefixDelimiter, indexOfBucketName() + 1);
    }
}
