package io.intino.alexandria.datalake.aws.measurement;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.FileStore;
import io.intino.alexandria.event.measurement.MeasurementEvent;

import java.io.File;
import java.util.stream.Stream;

public class AwsMeasurementEventStore implements Datalake.Store<MeasurementEvent>, FileStore {
    @Override
    public Stream<Tank<MeasurementEvent>> tanks() {
        return null;
    }

    @Override
    public Tank<MeasurementEvent> tank(String name) {
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
