package io.intino.alexandria.datalake.aws.message;

import io.intino.alexandria.datalake.Datalake.Store;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.event.message.MessageEvent;

import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.AwsDatalake.PrefixDelimiter;

public class MessageEventStore implements Store<MessageEvent> {
    public static final String MessagePrefix = "message";
    private final S3 s3;
    private final String bucketName;

    public MessageEventStore(S3 s3, String bucketName) {
        this.s3 = s3;
        this.bucketName = bucketName;
    }

    @Override
    public Stream<Tank<MessageEvent>> tanks() {
        return s3.keysIn(bucketName, MessagePrefix)
                .map(prefix -> prefix.substring(from(prefix), to(prefix)))
                .distinct()
                .map(tank -> new MessageEventTank(s3, bucketName, prefixOf(tank)));
    }

    @Override
    public Tank<MessageEvent> tank(String name) {
        return new MessageEventTank(s3, bucketName, prefixOf(name));
    }

    private int from(String s) {
        return s.indexOf(PrefixDelimiter) + 1;
    }

    private int to(String s) {
        return s.indexOf(PrefixDelimiter, from(s));
    }

    private String prefixOf(String name) {
        return MessagePrefix + PrefixDelimiter + name;
    }
}
