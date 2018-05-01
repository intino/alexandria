package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.ui.model.TimeRange;

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
