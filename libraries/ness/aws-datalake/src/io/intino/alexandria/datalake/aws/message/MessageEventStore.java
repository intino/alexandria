package io.intino.alexandria.datalake.aws.message;

import io.intino.alexandria.datalake.Datalake.Store;
import io.intino.alexandria.datalake.file.FileStore;
import io.intino.alexandria.event.message.MessageEvent;

import java.io.File;
import java.util.stream.Stream;

public class MessageEventStore implements Store<MessageEvent>, FileStore {
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
