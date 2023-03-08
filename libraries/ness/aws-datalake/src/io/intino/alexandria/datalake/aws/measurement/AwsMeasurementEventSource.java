package io.intino.alexandria.datalake.aws.measurement;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.measurement.MeasurementEvent;

import java.util.stream.Stream;

public class AwsMeasurementEventSource implements Datalake.Store.Source<MeasurementEvent> {
    @Override
    public String name() {
        return null;
    }

    @Override
    public Stream<Datalake.Store.Tub<MeasurementEvent>> tubs() {
        return null;
    }

    @Override
    public Datalake.Store.Tub<MeasurementEvent> first() {
        return null;
    }

    @Override
    public Datalake.Store.Tub<MeasurementEvent> last() {
        return null;
    }

    @Override
    public Datalake.Store.Tub<MeasurementEvent> on(Timetag tag) {
        return null;
    }

    @Override
    public Scale scale() {
        return Datalake.Store.Source.super.scale();
    }

    @Override
    public Stream<Datalake.Store.Tub<MeasurementEvent>> tubs(Timetag from, Timetag to) {
        return Datalake.Store.Source.super.tubs(from, to);
    }
}
