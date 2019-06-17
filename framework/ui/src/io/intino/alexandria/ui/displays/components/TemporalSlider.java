package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.slider.Ordinal;
import io.intino.alexandria.ui.displays.components.slider.Range;
import io.intino.alexandria.ui.displays.components.slider.ordinals.*;
import io.intino.alexandria.ui.displays.notifiers.TemporalSliderNotifier;
import io.intino.alexandria.ui.model.TimeScale;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static java.time.ZoneOffset.UTC;

public class TemporalSlider<DN extends TemporalSliderNotifier, B extends Box> extends AbstractTemporalSlider<DN, B> {
    private Instant min;
    private Instant max;
    private java.util.List<Collection> collections = new ArrayList<>();

    public TemporalSlider(B box) {
        super(box);
    }

    public TemporalSlider range(Instant min, Instant max) {
        this.min = min;
        this.max = max;
        updateRange();
        return this;
    }

    @Override
    public TemporalSlider ordinal(Ordinal ordinal) {
        super.ordinal(ordinal);
        updateRange();
        return this;
    }

    public TemporalSlider bindTo(Collection... collections) {
        this.collections = Arrays.asList(collections);
        return this;
    }

    @Override
    public void refresh() {
        super.refresh();
        Range range = range();
        notifier.refreshRange(new io.intino.alexandria.schemas.Range().min(range().min()).max(range.max()));
    }

    @Override
    void notifyChange() {
        notifyCollections();
        super.notifyChange();
    }

    private void notifyCollections() {
        collections.forEach(c -> c.filter(timetag()));
    }

    private Timetag timetag() {
        LocalDateTime localDate = Instant.ofEpochMilli(value()).atZone(UTC).toLocalDateTime();
        return Timetag.of(localDate, scale());
    }

    private Scale scale() {
        Ordinal ordinal = ordinal();
        if (ordinal instanceof YearOrdinal) return Scale.Year;
        if (ordinal instanceof MonthOrdinal) return Scale.Month;
        if (ordinal instanceof DayOrdinal) return Scale.Day;
        if (ordinal instanceof HourOrdinal) return Scale.Hour;
        if (ordinal instanceof MinuteOrdinal) return Scale.Minute;
        return Scale.Day;
    }

    @Override
    void updateRange() {
        Ordinal ordinal = ordinal();
        if (ordinal == null) return;
        long count = TimeScale.valueOf(ordinal().name()).instantsBetween(min, max);
        range(0, count);
    }

}