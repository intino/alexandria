package io.intino.alexandria.ui.displays;

import io.intino.alexandria.ui.model.TimeRange;

public class AlexandriaTemporalStamp<N extends AlexandriaDisplayNotifier> extends AlexandriaStamp<N> {
    TimeRange range;

    public TimeRange range() {
        return range;
    }

    public AlexandriaTemporalStamp<N> range(TimeRange range) {
        this.range = range;
        return this;
    }

}
