package io.intino.alexandria.datalake.aws.message;

import io.intino.alexandria.datalake.Datalake.Store;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.datalake.file.FileStore;
import io.intino.alexandria.event.message.MessageEvent;

import java.io.File;
import java.util.stream.Stream;

public class MessageEventStore implements Store<MessageEvent>, FileStore {
    private final S3 s3;
    private final String bucketName;

    public MessageEventStore(S3 s3, String bucketName) {
        this.s3 = s3;
        this.bucketName = bucketName;
    }

    @Override
    public Stream<Tank<MessageEvent>> tanks() {
        return null;
    }

    @Override
    public Tank<MessageEvent> tank(String s) {
        return null;
    }

    @Override
    public String fileExtension() {
        return null;
    }

    @Override
    public File directory() {
        return null;
    }
}
