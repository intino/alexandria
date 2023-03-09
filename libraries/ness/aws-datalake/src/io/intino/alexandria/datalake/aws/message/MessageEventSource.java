package io.intino.alexandria.datalake.aws.message;

import com.amazonaws.services.s3.AmazonS3;
import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.event.message.MessageEvent;

import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.AwsDatalake.PrefixDelimiter;

public class MessageEventSource implements Datalake.Store.Source<MessageEvent> {
    private final AmazonS3 client;
    private final String bucketName;
    private final String prefix;

    public MessageEventSource(AmazonS3 client, String bucketName, String prefix) {
        this.client = client;
        this.bucketName = bucketName;
        this.prefix = prefix;
    }


    @Override
    public String name() {
        String[] rootArray = prefix.split(PrefixDelimiter);
        return rootArray[rootArray.length - 1];
    }

    @Override
    public Stream<Datalake.Store.Tub<MessageEvent>> tubs() {
        return S3.keysIn(client, bucketName, prefix).map(key -> new MessageEventTub(client, bucketName, key));
    }

    @Override
    public Datalake.Store.Tub<MessageEvent> first() {
        return tubs().findFirst().orElse(null);
    }

    @Override
    public Datalake.Store.Tub<MessageEvent> last() {
        return tubs().reduce((first, second) -> second).orElse(currentTub());
    }

    @Override
    public Datalake.Store.Tub<MessageEvent> on(Timetag tag) {
        return new MessageEventTub(client, bucketName, ""); // TODO
    }

    @Override
    public Scale scale() {
        return first().scale();
    }

    private Datalake.Store.Tub<MessageEvent> currentTub() {
        return null; // TODO
    }

    @Override
    public Stream<Datalake.Store.Tub<MessageEvent>> tubs(Timetag from, Timetag to) {
        return Datalake.Store.Source.super.tubs(from, to);
    }
}
