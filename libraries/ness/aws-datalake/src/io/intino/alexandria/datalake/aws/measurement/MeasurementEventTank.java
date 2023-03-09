package io.intino.alexandria.datalake.aws.measurement;

import com.amazonaws.services.s3.AmazonS3;
import io.intino.alexandria.datalake.Datalake.Store;
import io.intino.alexandria.datalake.Datalake.Store.Tank;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.event.measurement.MeasurementEvent;

import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.AwsDatalake.PrefixDelimiter;
import static io.intino.alexandria.datalake.aws.measurement.MeasurementEventStore.MeasurementsPrefix;


public class MeasurementEventTank implements Tank<MeasurementEvent> {
    public final AmazonS3 client;
    public final String bucketName;
    public final String prefix;

    public MeasurementEventTank(AmazonS3 client, String bucketName, String prefix) {
        this.client = client;
        this.bucketName = bucketName;
        this.prefix = prefix;
    }

    @Override
    public String name() {
        return prefix.substring(indexOfBucketName());
    }

    @Override
    public Store.Source<MeasurementEvent> source(String name) {
        return new MeasurementEventSource(client, bucketName, prefix + PrefixDelimiter + name);
    }

    @Override
    public Stream<Store.Source<MeasurementEvent>> sources() {
        return S3.keysIn(client, bucketName, prefix)
                .map(prefix -> prefix.substring(indexOfBucketName(), to(prefix)))
                .distinct()
                .map(prefix -> new MeasurementEventSource(client, bucketName, prefix));
    }

    private static int indexOfBucketName() {
        return (MeasurementsPrefix + PrefixDelimiter).length() + 1;
    }

    private static int to(String s) {
        return s.indexOf(PrefixDelimiter, indexOfBucketName() + 1);
    }
}
