package io.intino.alexandria.datalake.aws.trees.onedepth;

import com.amazonaws.services.s3.model.S3Object;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake.EventStore.Tub;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.event.EventReader;
import io.intino.alexandria.event.EventStream;

import static io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthDatalake.AWS_DELIMITER;
import static io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthDatalake.PREFIX_DELIMITER;

public class AwsOneDepthEventTub implements Tub {

    private final S3 s3;
    private final String bucketName;
    private final String prefix;

    public AwsOneDepthEventTub(S3 s3, String bucketName, String prefix) {
        this.s3 = s3;
        this.bucketName = bucketName;
        this.prefix = prefix;
    }

    @Override
    public Timetag timetag() {
        return new Timetag(name());
    }

    private String name() {
        String[] route = prefix.split(AWS_DELIMITER)[0].split(PREFIX_DELIMITER);
        return route[route.length - 1];
    }

    @Override
    public EventStream events() {
        return new EventReader(object().getObjectContent());
    }

    private S3Object object() {
        return s3.getObjectFrom(bucketName, prefix);
    }
}
