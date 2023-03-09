package io.intino.alexandria.datalake.aws.measurement;

import com.amazonaws.services.s3.AmazonS3;
import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.event.measurement.MeasurementEvent;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static io.intino.alexandria.datalake.aws.AwsDatalake.PrefixDelimiter;

public class MeasurementEventSource implements Datalake.Store.Source<MeasurementEvent> {

    private final AmazonS3 client;
    private final String bucketName;
    private final String prefix;

    public MeasurementEventSource(AmazonS3 client, String bucketName, String prefix) {
        this.client = client;
        this.bucketName = bucketName;
        this.prefix = prefix;
    }

    @Override
    public String name() {
        String[] rootArray = prefix.split(PrefixDelimiter);
        return rootArray[rootArray.length - 1];
    }

    @Override
    public Stream<Datalake.Store.Tub<MeasurementEvent>> tubs() {
        return S3.keysIn(client, bucketName, prefix).map(key -> new MeasurementEventTub(client, bucketName, key));
    }

    @Override
    public Datalake.Store.Tub<MeasurementEvent> first() {
        return tubs().findFirst().orElse(null);
    }

    @Override
    public Datalake.Store.Tub<MeasurementEvent> last() {
        List<Datalake.Store.Tub<MeasurementEvent>> list = tubs().collect(Collectors.toList());
        return list.get(list.size() - 1);
    }

    @Override
    public Datalake.Store.Tub<MeasurementEvent> on(Timetag tag) {
        return new MeasurementEventTub(client, bucketName, tubNameOf(tag));
    }

    private String tubNameOf(Timetag tag) {
        return prefix + PrefixDelimiter + tag.value() + "NOT KNOWN"; //TODO
    }

    @Override
    public Scale scale() {
        return first().scale();
    }

    @Override
    public Stream<Datalake.Store.Tub<MeasurementEvent>> tubs(Timetag from, Timetag to) {
        return StreamSupport.stream(from.iterateTo(to).spliterator(), false).map(this::on);
    }
}
