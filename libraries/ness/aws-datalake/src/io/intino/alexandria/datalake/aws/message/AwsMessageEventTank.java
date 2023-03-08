package io.intino.alexandria.datalake.aws.message;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.message.MessageEvent;

import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class AwsMessageEventTank implements Datalake.Store.Tank<MessageEvent> {
    @Override
    public String name() {
        return null;
    }

    @Override
    public Scale scale() {
        return Datalake.Store.Tank.super.scale();
    }

    @Override
    public Datalake.Store.Source<MessageEvent> source(String name) {
        return null;
    }

    @Override
    public Stream<Datalake.Store.Source<MessageEvent>> sources() {
        return null;
    }

    @Override
    public Stream<MessageEvent> content() {
        return Datalake.Store.Tank.super.content();
    }

    @Override
    public Stream<MessageEvent> content(BiPredicate<Datalake.Store.Source<MessageEvent>, Timetag> filter) {
        return Datalake.Store.Tank.super.content(filter);
    }
}
