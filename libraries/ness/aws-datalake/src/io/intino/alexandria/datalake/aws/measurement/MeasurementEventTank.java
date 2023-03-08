package io.intino.alexandria.datalake.aws.measurement;

import io.intino.alexandria.datalake.Datalake.Store;
import io.intino.alexandria.datalake.Datalake.Store.Tank;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.event.measurement.MeasurementEvent;

import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.AwsDatalake.PrefixDelimiter;
import static io.intino.alexandria.datalake.aws.measurement.MeasurementEventStore.MeasurementPrefix;


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
        return prefix.substring(indexOfTank());
    }

    @Override
    public Store.Source<MeasurementEvent> source(String name) {
        return new MeasurementEventSource(s3, bucketName, prefixOf(name));
    }

    @Override
    public Stream<Store.Source<MeasurementEvent>> sources() {
        return s3.keysIn(bucketName, prefix)
                .map(prefix -> prefix.substring(indexOfTank(), to()))
                .distinct()
                .map(source -> new MeasurementEventSource(s3, bucketName, prefixOf(source)));
    }

    private int indexOfTank() {
        return (MeasurementPrefix + PrefixDelimiter).length() + 1;
    }

    private int to() {
        return prefix.indexOf(PrefixDelimiter, indexOfTank());
    }

    private String prefixOf(String name) {
        return prefix + PrefixDelimiter + name;
    }
}
