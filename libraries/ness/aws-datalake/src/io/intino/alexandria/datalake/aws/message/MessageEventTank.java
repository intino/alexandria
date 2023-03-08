package io.intino.alexandria.datalake.aws.message;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.Datalake.Store.Tank;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.event.message.MessageEvent;

import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.AwsDatalake.PrefixDelimiter;
import static io.intino.alexandria.datalake.aws.message.MessageEventStore.MessagePrefix;

public class MessageEventTank implements Tank<MessageEvent>{
    public final S3 s3;
    public final String bucketName;
    public final String prefix;

    public MessageEventTank(S3 s3, String bucketName, String prefix) {
        this.s3 = s3;
        this.bucketName = bucketName;
        this.prefix = prefix;
    }

    @Override
    public String name() {
        return prefix.substring(indexOfBucketName());
    }

    @Override
    public Datalake.Store.Source<MessageEvent> source(String name) {
        return null;
    }

    @Override
    public Stream<Datalake.Store.Source<MessageEvent>> sources() {
        return null;
    }

    private static int indexOfBucketName() {
        return (MessagePrefix + PrefixDelimiter).length() + 1;
    }

    private static int to(String s) {
        return s.indexOf(PrefixDelimiter, indexOfBucketName() + 1);
    }
}
