package io.intino.alexandria.datalake.aws.measurement;

import com.amazonaws.services.s3.model.S3Object;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.AwsTub;
import io.intino.alexandria.datalake.Datalake.Store.Tub;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.EventReader;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.measurement.MeasurementEvent;
import io.intino.alexandria.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.AwsDatalake.Aws_delimeter;
import static io.intino.alexandria.datalake.aws.AwsDatalake.Prefix_delimeter;
import static io.intino.alexandria.event.Event.Format.Measurement;

public class MeasurementEventTub implements Tub<MeasurementEvent>, AwsTub {

    private final S3 s3;
    private final String bucketName;
    private final String prefix;

    public MeasurementEventTub(S3 s3, String bucketName, String prefix) {
        this.s3 = s3;
        this.bucketName = bucketName;
        this.prefix = prefix;
    }

    @Override
    public Timetag timetag() {
        return new Timetag(name());
    }

    private String name() {
        String[] route = prefix.split(Aws_delimeter)[0].split(Prefix_delimeter);
        return route[route.length - 1].replace(Measurement.extension(), "");
    }

    @Override
    public Stream<MeasurementEvent> events() {
        try {
            return getEventStream(object().getObjectContent());
        } catch (IOException e) {
            Logger.error(e);
            return Stream.empty();
        }
    }

    private <T extends Event> Stream<T> getEventStream(InputStream content) throws IOException {
        return new EventStream<>(EventReader.of(Measurement, content));
    }

    @Override
    public String fileExtension() {
        return Measurement.extension();
    }

    @Override
    public S3Object object() {
        return s3.getObjectFrom(bucketName, prefix);
    }
}
