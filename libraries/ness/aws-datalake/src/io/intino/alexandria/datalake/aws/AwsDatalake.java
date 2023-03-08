package io.intino.alexandria.datalake.aws;


import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.measurement.MeasurementEvent;
import io.intino.alexandria.event.message.MessageEvent;

public class AwsDatalake implements Datalake {
    private final S3 s3;
    private final String bucketName;


    public AwsDatalake(S3 s3, String bucketName) {
        this.s3 = s3;
        this.bucketName = bucketName;
    }

    @Override
    public Store<MessageEvent> messageStore() {
        return null;
    }

    @Override
    public Store<MeasurementEvent> measurementStore() {
        return null;
    }
}
