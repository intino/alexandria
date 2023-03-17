package io.intino.alexandria.datalake.aws.message;

import com.amazonaws.services.s3.AmazonS3;
import io.intino.alexandria.awscore.S3;
import io.intino.alexandria.datalake.Datalake.Store;
import io.intino.alexandria.datalake.aws.AwsStore;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.message.MessageEvent;

import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.AwsDatalake.PrefixDelimiter;

public class MessageEventStore implements Store<MessageEvent>, AwsStore {
    public static final String MessagePrefix = "message";
    private final AmazonS3 client;
    private final String bucketName;

    public MessageEventStore(AmazonS3 client, String bucketName) {
        this.client = client;
        this.bucketName = bucketName;
    }

    @Override
    public Stream<Tank<MessageEvent>> tanks() {
        return S3.keysIn(client, bucketName, MessagePrefix)
                .map(prefix -> prefix.substring(from(prefix), to(prefix)))
                .distinct()
                .map(tank -> new MessageEventTank(client, bucketName, prefixOf(tank)));
    }

    @Override
    public Tank<MessageEvent> tank(String name) {
        return new MessageEventTank(client, bucketName, prefixOf(name));
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

    @Override
    public String fileExtension() {
        return Event.Format.Message.extension();
    }

    @Override
    public String prefix() {
        return "message/";
    }
}
