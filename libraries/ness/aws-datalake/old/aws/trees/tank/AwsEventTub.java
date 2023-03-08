package io.intino.alexandria.datalake.aws.trees.tank;

import com.amazonaws.services.s3.model.S3Object;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.event.EventReader;
import io.intino.alexandria.event.EventStream;

import static io.intino.alexandria.datalake.Datalake.EventStore.Tub;
import static io.intino.alexandria.datalake.aws.trees.tank.AwsDatalake.KeyDelimiter;
import static io.intino.alexandria.zim.ZimReader.ZimExtension;

public class AwsEventTub implements Tub {
    private static final String EventExtension = ZimExtension;
    private final S3 s3;
    private final String bucketName;
    private final String key;

    public AwsEventTub(S3 s3, String bucketName, String key) {
        this.s3 = s3;
        this.bucketName = bucketName;
        this.key = key;
    }

    @Override
    public Timetag timetag() {
        return new Timetag(name());
    }

    @Override
    public EventStream events() {
        return new EventReader(object().getObjectContent());
    }

    private String name() {
        String[] route = key.split(KeyDelimiter);
        return route[route.length - 1].replace(EventExtension, "");
    }

    private S3Object object() {
        return s3.getObjectFrom(bucketName, key);
    }
}
