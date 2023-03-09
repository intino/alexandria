package io.intino.alexandria.datalake.aws.message;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.AwsTub;
import io.intino.alexandria.datalake.Datalake.Store.Tub;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.EventReader;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.message.MessageEvent;
import io.intino.alexandria.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.AwsDatalake.*;
import static io.intino.alexandria.event.Event.Format.Message;

public class MessageEventTub implements Tub<MessageEvent>, AwsTub {

    private final AmazonS3 client;
    private final String bucketName;
    private final String prefix;

    public MessageEventTub(AmazonS3 client, String bucketName, String prefix) {
        this.client = client;
        this.bucketName = bucketName;
        this.prefix = prefix;
    }

    @Override
    public Timetag timetag() {
        return new Timetag(name());
    }

    private String name() {
        String[] route = prefix.substring(0, prefix.indexOf(AwsDelimiter)).split(PrefixDelimiter);
        return route[route.length - 1].replace(Message.extension(), "");
    }

    @Override
    public Stream<MessageEvent> events() {
        try {
            return getEventStream(object().getObjectContent());
        } catch (IOException e) {
            Logger.error(e);
            return Stream.empty();
        }
    }

    @Override
    public String fileExtension() {
        return Message.extension();
    }

    @Override
    public S3Object object() {
        return S3.getObjectFrom(client, bucketName, prefix);
    }

    private <T extends Event> Stream<T> getEventStream(InputStream content) throws IOException {
        return new EventStream<>(EventReader.of(Message, content));
    }
}
