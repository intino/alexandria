package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.Selected;
import io.intino.alexandria.ui.displays.components.slider.Ordinal;
import io.intino.alexandria.ui.displays.components.slider.Range;
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
        _value(0);
    }

    @Override
    public void didMount() {
        super.didMount();
        updateRange();
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
    public void reset() {
        _value(range().min());
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

    public void moved(long value) {
        notifier.refreshSelected(schemaOf(value));
        notifier.refreshToolbar(toolbarState());
    }

    public void update(long value) {
        value(value);
    }

    @Override
    void notifyChange() {
        super.notifyChange();
        notifyCollections();
    }

    public String formattedValue(long value) {
        return format(value);
    }

    @Override
    String format(long value) {
        Ordinal ordinal = ordinal();
        if (ordinal == null) ordinal = defaultOrdinals().get(0);
        return ordinal != null ? (customFormatter != null ? customFormatter.format(millisOf(value)) : ordinal.formatter(language()).format(millisOf(value))) : String.valueOf(millisOf(value));
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

    public Scale scale() {
        Ordinal ordinal = ordinal();
        if (ordinal instanceof YearOrdinal) return Scale.Year;
        if (ordinal instanceof MonthOrdinal) return Scale.Month;
        if (ordinal instanceof DayOrdinal) return Scale.Day;
        if (ordinal instanceof HourOrdinal) return Scale.Hour;
        if (ordinal instanceof MinuteOrdinal) return Scale.Minute;
        return Scale.Day;
    }

    @Override
    protected void _value(Object value) {
        this.value = value instanceof Integer ? Long.valueOf((int)value) : value;
    }

    protected TemporalSlider<DN, B> _range(Instant min, Instant max) {
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

    @Override
    void updateRange() {
        Ordinal ordinal = ordinal();
        if (ordinal == null) return;
        long count = toLong(max);
        _range(0, count);
        if (notifier != null) {
            notifier.refreshRange(rangeSchema());
            notifier.refreshToolbar(toolbarState());
        }
    }

    @Override
    <T extends Selected> void refreshSelected(T schema) {
        notifier.refreshSelected(schema);
    }

    @Override
    Selected schemaOf(Object value) {
        String formattedValue = formattedValue((Long) value);
        return new Selected().value((Long)value).formattedValue(formattedValue);
    }

    @Override
    void notifyListener() {
        if (changeListener() == null) return;
        changeListener().accept(new ChangeEvent(this, toInstant(millisOf(value()))));
    }

    @Override
    boolean checkRange(Object value) {
        if ((Long)value > range.max) return false;
        if ((Long)value < range.min) return false;
        return true;
    }

    @Override
    public void previous() {
        value((Long)value()-1);
    }

    @Override
    public void next() {
        value((Long)value()+1);
    }

    @Override
    boolean canPrevious() {
        return (Long)value() > range.min;
    }

    @Override
    boolean canNext() {
        return (Long)value() < range.max;
    }

    @Override
    void nextValue() {
        Long value = value();
        if (value >= range.max) {
            if (canLoop()) value(range.min()-1);
            else return;
        }
        value(value +1);
    }

    @Override
    void _range(long min, long max) {
        this.range = new Range().min(min).max(max);
        long value = value();
        if (value < min) _value(min);
        if (value > max) _value(max);
    }

    private long toLong(Instant instant) {
        return toLong(timeScale(), instant);
    }

    private long toLong(TimeScale timeScale, Instant instant) {
        return timeScale.instantsBetween(min, instant) - 1;
    }

}