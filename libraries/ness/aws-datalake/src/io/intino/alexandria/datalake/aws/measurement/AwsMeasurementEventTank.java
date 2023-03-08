package io.intino.alexandria.datalake.aws.measurement;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.measurement.MeasurementEvent;

import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class AwsMeasurementEventTank implements Datalake.Store.Tank<MeasurementEvent> {
    @Override
    public String name() {
        return null;
    }

    @Override
    public Scale scale() {
        return Datalake.Store.Tank.super.scale();
    }

    @Override
    public Datalake.Store.Source<MeasurementEvent> source(String name) {
        return null;
    }

    @Override
    public Stream<Datalake.Store.Source<MeasurementEvent>> sources() {
        return null;
    }

    @Override
    public Stream<MeasurementEvent> content() {
        return Datalake.Store.Tank.super.content();
    }

    @Override
    public Stream<MeasurementEvent> content(BiPredicate<Datalake.Store.Source<MeasurementEvent>, Timetag> filter) {
        return Datalake.Store.Tank.super.content(filter);
    }
}
