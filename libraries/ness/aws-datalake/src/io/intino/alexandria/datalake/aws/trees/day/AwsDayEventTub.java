package io.intino.alexandria.datalake.aws.trees.day;

import com.amazonaws.services.s3.model.S3Object;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake.EventStore.Tub;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.event.EventReader;
import io.intino.alexandria.event.EventStream;

public class AwsDayEventTub implements Tub {
    private final String bucketName;
    private final String key;
    private final S3 s3;

    public AwsDayEventTub(String bucketName, String key, S3 s3) {
        this.bucketName = bucketName;
        this.key = key;
        this.s3 = s3;
    }

    public S3Object object() {
        return s3.getObjectFrom(bucketName, key);
    }

    @Override
    public Timetag timetag() {
        String[] route = object().getKey().split("/");
        return new Timetag(route[route.length - 1]);
    }

    @Override
    public EventStream events() {
        return new EventReader(object().getObjectContent());
    }
}
