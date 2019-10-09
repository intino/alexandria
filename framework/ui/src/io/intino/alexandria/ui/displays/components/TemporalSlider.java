package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.slider.Ordinal;
import io.intino.alexandria.ui.displays.components.slider.ordinals.*;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
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
    private Ordinal.Formatter customFormatter = null;

    public TemporalSlider(B box) {
        super(box);
    }

    public Instant min() {
        return min;
    }

    public Instant max() {
        return max;
    }

    public void value(Instant instant) {
        value(toLong(instant));
    }

    @Override
    public TemporalSlider ordinal(Ordinal ordinal) {
        super.ordinal(ordinal);
        updateRange();
        return this;
    }

    public TemporalSlider formatter(Ordinal.Formatter formatter) {
        this.customFormatter = formatter;
        return this;
    }

    public TemporalSlider bindTo(Collection... collections) {
        this.collections = Arrays.asList(collections);
        return this;
    }

    public TemporalSlider range(Instant min, Instant max) {
        _range(min, max);
        return this;
    }

    @Override
    void notifyChange() {
        notifyCollections();
        super.notifyChange();
    }

    @Override
    public String formattedValue() {
        Ordinal ordinal = ordinal();
        long value = millisOf(value());
        return ordinal != null ? (customFormatter != null ? customFormatter.format(value) : ordinal.formatter(language()).format(value)) : String.valueOf(value);
    }

    @Override
    public void selectOrdinal(String name) {
        Instant current = toInstant(millisOf(value()));
        super.selectOrdinal(name, toLong(timeScale(name), current));
    }

    public Timetag timetag() {
        LocalDateTime localDate = toInstant(millisOf(value())).atZone(UTC).toLocalDateTime();
        return Timetag.of(localDate, scale());
    }

    protected TemporalSlider _range(Instant min, Instant max) {
        this.min = min;
        this.max = max;
        updateRange();
        return this;
    }

    private long millisOf(long value) {
        return timeScale().addTo(min, value).toEpochMilli();
    }

    private Instant toInstant(long millis) {
        return Instant.ofEpochMilli(millis);
    }

    private TimeScale timeScale() {
        return timeScale(ordinal().name());
    }

    private TimeScale timeScale(String ordinal) {
        return TimeScale.valueOf(ordinal);
    }

    private void notifyCollections() {
        collections.forEach(c -> c.filter(timetag()));
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
        long count = toLong(max);
        _range(0, count);
        if (notifier != null) notifier.refreshRange(rangeSchema());
    }

    @Override
    void notifyListener() {
        if (changeListener() == null) return;
        changeListener().accept(new ChangeEvent(this, toInstant(millisOf(value()))));
    }

    private long toLong(Instant instant) {
        return toLong(timeScale(), instant);
    }

    private long toLong(TimeScale timeScale, Instant instant) {
        return timeScale.instantsBetween(min, instant) - 1;
    }

}