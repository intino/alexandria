package io.intino.alexandria.framework.box.displays.builders;

import io.intino.alexandria.framework.box.helpers.ZoomRange;
import io.intino.alexandria.framework.box.model.TimeRange;
import io.intino.alexandria.framework.box.schemas.Range;

import java.time.Instant;

public class RangeBuilder {

    public static Range build(TimeRange range) {
        Instant from = range.scale().normalise(range.from());
        Instant to = range.scale().normalise(range.to());
        return new Range().min(from.toEpochMilli()).max(to.toEpochMilli()).scale(range.scale().name());
    }

    public static Range build(ZoomRange range) {
        return new Range().min(range.min).max(range.max);
    }

}
