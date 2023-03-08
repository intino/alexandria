package io.intino.alexandria.datalake.aws.message;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.message.MessageEvent;
import java.util.stream.Stream;

public class AwsMessageEventSource implements Datalake.Store.Source<MessageEvent> {
    @Override
    public String name() {
        return null;
    }

    @Override
    public Stream<Datalake.Store.Tub<MessageEvent>> tubs() {
        return null;
    }

    @Override
    public Datalake.Store.Tub<MessageEvent> first() {
        return null;
    }

    @Override
    public Datalake.Store.Tub<MessageEvent> last() {
        return null;
    }

    @Override
    public Datalake.Store.Tub<MessageEvent> on(Timetag tag) {
        return null;
    }

    @Override
    public Scale scale() {
        return Datalake.Store.Source.super.scale();
    }

    @Override
    public Stream<Datalake.Store.Tub<MessageEvent>> tubs(Timetag from, Timetag to) {
        return Datalake.Store.Source.super.tubs(from, to);
    }
}
