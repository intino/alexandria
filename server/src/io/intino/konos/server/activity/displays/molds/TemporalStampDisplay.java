package io.intino.konos.server.activity.displays.molds;

import io.intino.konos.server.activity.displays.DisplayNotifier;
import io.intino.konos.server.activity.displays.elements.model.TimeRange;

public class TemporalStampDisplay<N extends DisplayNotifier> extends StampDisplay<N> {
    TimeRange range;

    public TimeRange range() {
        return range;
    }

    public TemporalStampDisplay<N> range(TimeRange range) {
        this.range = range;
        return this;
    }

}
