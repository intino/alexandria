package io.intino.alexandria.datalake.aws.trees.day;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;

import java.util.stream.Stream;

public class AwsDayEntityTank implements Datalake.EntityStore.Tank {
    private final String name;

    public AwsDayEntityTank(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Stream<Datalake.EntityStore.Tub> tubs() {
        return null;
    }

    @Override
    public Datalake.EntityStore.Tub first() {
        return null;
    }

    @Override
    public Datalake.EntityStore.Tub last() {
        return null;
    }

    @Override
    public Datalake.EntityStore.Tub on(Timetag tag) {
        return null;
    }

    @Override
    public Stream<Datalake.EntityStore.Tub> tubs(int count) {
        return null;
    }

    @Override
    public Stream<Datalake.EntityStore.Tub> tubs(Timetag from, Timetag to) {
        return null;
    }
}
