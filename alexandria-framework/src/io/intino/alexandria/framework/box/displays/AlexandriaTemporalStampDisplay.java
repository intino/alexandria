package io.intino.alexandria.framework.box.displays;

import io.intino.alexandria.foundation.activity.displays.DisplayNotifier;
import io.intino.alexandria.framework.box.model.TimeRange;

public class AlexandriaTemporalStampDisplay<N extends DisplayNotifier> extends AlexandriaStampDisplay<N> {
    TimeRange range;

    public TimeRange range() {
        return range;
    }

    public AlexandriaTemporalStampDisplay<N> range(TimeRange range) {
        this.range = range;
        return this;
    }

}
