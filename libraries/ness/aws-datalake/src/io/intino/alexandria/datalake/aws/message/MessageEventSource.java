package io.intino.alexandria.datalake.aws.message;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.event.message.MessageEvent;

import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.AwsDatalake.PrefixDelimiter;

public class MessageEventSource implements Datalake.Store.Source<MessageEvent> {
    private final S3 s3;
    private final String bucketName;
    private final String prefix;

    public MessageEventSource(S3 s3, String bucketName, String prefix) {
        this.s3 = s3;
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
        return s3.keysIn(bucketName, prefix).map(key -> new MessageEventTub(s3, bucketName, key));
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
        return new MessageEventTub(s3, bucketName, ""); // TODO
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
