package io.intino.konos.alexandria.framework.box.displays;

import io.intino.konos.alexandria.foundation.activity.displays.AlexandriaDisplayNotifier;
import io.intino.konos.alexandria.framework.box.model.TimeRange;

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
