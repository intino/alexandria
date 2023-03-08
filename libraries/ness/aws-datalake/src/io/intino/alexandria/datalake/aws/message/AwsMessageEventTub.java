package io.intino.alexandria.datalake.aws.message;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.FileTub;
import io.intino.alexandria.event.message.MessageEvent;

import java.io.File;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AwsMessageEventTub implements Datalake.Store.Tub<MessageEvent>, FileTub {
    @Override
    public Timetag timetag() {
        return null;
    }

    @Override
    public Stream<MessageEvent> events() {
        return null;
    }

    @Override
    public Scale scale() {
        return Datalake.Store.Tub.super.scale();
    }

    @Override
    public Stream<MessageEvent> events(Predicate<MessageEvent> filter) {
        return Datalake.Store.Tub.super.events(filter);
    }

    @Override
    public String fileExtension() {
        return null;
    }

    @Override
    public File file() {
        return null;
    }
}
