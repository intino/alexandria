package io.intino.alexandria.datalake.aws.trees.day;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake.EventStore;
import io.intino.alexandria.datalake.Datalake.EventStore.Tank;

import java.util.stream.Stream;

public class AwsEventDayTank implements Tank {
    private final String name;

    public AwsEventDayTank(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Scale scale() {
        return null;
    }

    @Override
    public Stream<EventStore.Tub> tubs() {
        return null;
    }

    @Override
    public EventStore.Tub first() {
        return null;
    }

    @Override
    public EventStore.Tub last() {
        return null;
    }

    @Override
    public EventStore.Tub on(Timetag tag) {
        return null;
    }
}
