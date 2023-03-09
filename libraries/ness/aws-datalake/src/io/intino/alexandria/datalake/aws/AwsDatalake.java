package io.intino.alexandria.datalake.aws;


import com.amazonaws.services.s3.AmazonS3;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.aws.measurement.MeasurementEventStore;
import io.intino.alexandria.datalake.aws.message.MessageEventStore;
import io.intino.alexandria.event.measurement.MeasurementEvent;
import io.intino.alexandria.event.message.MessageEvent;

public class AwsDatalake implements Datalake {
    public static final String PrefixDelimiter = "_";
    public static final String AwsDelimiter = "/";
    private final AmazonS3 client;
    private final String bucketName;

    public AwsDatalake(AmazonS3 client, String bucketName) {
        this.client = client;
        this.bucketName = bucketName;
    }

    @Override
    public Store<MessageEvent> messageStore() {
        return new MessageEventStore(client, bucketName);
    }

    @Override
    public Store<MeasurementEvent> measurementStore() {return new MeasurementEventStore(client, bucketName);}
}
