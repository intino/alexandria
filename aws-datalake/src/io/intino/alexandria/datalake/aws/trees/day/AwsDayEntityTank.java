package io.intino.alexandria.datalake.aws.trees.day;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.Datalake.EntityStore.Tub;
import io.intino.alexandria.datalake.aws.S3;

import java.util.stream.Stream;

public class AwsDayEntityTank implements Datalake.EntityStore.Tank {
    private final String name;
    private final S3 s3;

    public AwsDayEntityTank(String name, S3 s3) {
        this.name = name;
        this.s3 = s3;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Stream<Tub> tubs() {
        return null;
    }

    @Override
    public Tub first() {
        return null;
    }

    @Override
    public Tub last() {
        return null;
    }

    @Override
    public Tub on(Timetag tag) {
        return null;
    }

    @Override
    public Stream<Tub> tubs(int count) {
        return null;
    }

    @Override
    public Stream<Tub> tubs(Timetag from, Timetag to) {
        return null;
    }
}
