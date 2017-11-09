package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.activity.model.TimeRange;

public class AlexandriaTemporalStampDisplay<N extends AlexandriaDisplayNotifier> extends AlexandriaStampDisplay<N> {
    TimeRange range;

    public TimeRange range() {
        return range;
    }

    public AlexandriaTemporalStampDisplay<N> range(TimeRange range) {
        this.range = range;
        return this;
    }

}
