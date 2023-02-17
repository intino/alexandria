package io.intino.alexandria.datalake.aws.trees.day;

import com.amazonaws.services.s3.model.S3Object;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake.EventStore.Tub;
import io.intino.alexandria.event.EventReader;
import io.intino.alexandria.event.EventStream;

public class AwsDayEventTub implements Tub {
    private final S3Object object;

    public AwsDayEventTub(S3Object object) {
        this.object = object;
    }

    public S3Object object() {
        return object;
    }

    @Override
    public Timetag timetag() {
        String[] route = object.getKey().split("/");
        return new Timetag(route[route.length - 1]);
    }

    @Override
    public EventStream events() {
        return new EventReader(object.getObjectContent());
    }
}
