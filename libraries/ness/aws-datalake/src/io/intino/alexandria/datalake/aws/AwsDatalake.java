package io.intino.alexandria.datalake.aws;


import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.aws.measurement.MeasurementEventStore;
import io.intino.alexandria.datalake.aws.message.MessageEventStore;
import io.intino.alexandria.event.measurement.MeasurementEvent;
import io.intino.alexandria.event.message.MessageEvent;

public class AwsDatalake implements Datalake {

    public static final String Prefix_delimeter = "_";
    public static final String Aws_delimeter = "/";
    private final S3 s3;
    private final String bucketName;

    public AwsDatalake(S3 s3, String bucketName) {
        this.s3 = s3;
        this.bucketName = bucketName;
    }

    @Override
    public Store<MessageEvent> messageStore() {
        return new MessageEventStore(s3, bucketName);
    }

    @Override
    public Store<MeasurementEvent> measurementStore() {
        return new MeasurementEventStore(s3, bucketName);
    }
}
