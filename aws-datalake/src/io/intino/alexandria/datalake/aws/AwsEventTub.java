package io.intino.alexandria.datalake.aws;

import com.amazonaws.services.s3.model.S3Object;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.event.EventReader;
import io.intino.alexandria.event.EventStream;

import static io.intino.alexandria.datalake.Datalake.EventStore.Tub;
import static io.intino.alexandria.zim.ZimReader.ZimExtension;

public class AwsEventTub implements Tub {
    private static final String EventExtension = ZimExtension;
    private final S3Object object;

    public AwsEventTub(S3Object object) {
        this.object = object;
    }

    @Override
    public Timetag timetag() {
        return new Timetag(name());
    }

    @Override
    public EventStream events() {
        return new EventReader(object.getObjectContent());
    }

    private String name() {
        String[] route = object.getKey().split(AwsDatalake.AwsDelimiter);
        return route[route.length - 1].replace(EventExtension, "");
    }

    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(this.getClass())) return false;
        AwsEventTub tub = (AwsEventTub) obj;
        return this.object.getKey().equals(tub.object.getKey()) &&
                this.object.getBucketName().equals(tub.object.getBucketName());
    }
}
