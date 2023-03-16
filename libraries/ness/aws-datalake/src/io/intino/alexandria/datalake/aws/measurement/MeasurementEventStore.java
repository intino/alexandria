package io.intino.alexandria.datalake.aws.measurement;

import com.amazonaws.services.s3.AmazonS3;
import io.intino.alexandria.datalake.Datalake.Store;
import io.intino.alexandria.datalake.aws.AwsStore;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.measurement.MeasurementEvent;

import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.AwsDatalake.PrefixDelimiter;

public class MeasurementEventStore implements Store<MeasurementEvent>, AwsStore {
    public static final String MeasurementsPrefix = "measurement";
    private final AmazonS3 client;
    private final String bucketName;

    public MeasurementEventStore(AmazonS3 client, String bucketName) {
        this.client = client;
        this.bucketName = bucketName;
    }

    @Override
    public Stream<Tank<MeasurementEvent>> tanks() {
        return S3.keysIn(client, bucketName, MeasurementsPrefix)
                .map(prefix -> prefix.substring(from(prefix), to(prefix)))
                .distinct()
                .map(tank -> new MeasurementEventTank(client, bucketName, prefixOf(tank)));
    }

    @Override
    public Tank<MeasurementEvent> tank(String name) {
        return new MeasurementEventTank(client, bucketName, prefixOf(name));
    }

    private int from(String s) {
        return s.indexOf(PrefixDelimiter);
    }

    private int to(String s) {
        return s.indexOf(PrefixDelimiter, from(s) + 1);
    }

    private static String prefixOf(String name) {
        return MeasurementsPrefix + PrefixDelimiter + name;
    }

    @Override
    public String fileExtension() {
        return Event.Format.Measurement.extension();
    }

    @Override
    public String prefix() {
        return "measurement/";
    }
}
