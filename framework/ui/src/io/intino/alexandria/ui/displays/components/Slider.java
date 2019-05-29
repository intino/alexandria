package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.slider.Animation;
import io.intino.alexandria.ui.displays.components.slider.Ordinal;
import io.intino.alexandria.ui.displays.components.slider.Range;
import io.intino.alexandria.ui.displays.components.slider.ordinals.*;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.notifiers.SliderNotifier;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.time.ZoneOffset.UTC;
import static java.util.stream.Collectors.toList;

public class Slider<DN extends SliderNotifier, B extends Box> extends AbstractSlider<B> {
    private int value = -1;
    private Ordinal ordinal = null;
    private Range range;
    private Animation animation;
    private java.util.List<Ordinal> ordinalList = new ArrayList<>();
    private java.util.List<Collection> collections = new ArrayList<>();
    private ChangeListener changeListener = null;

    public Slider(B box) {
        super(box);
    }

    public Slider value(int value) {
        this.value = value;
        return this;
    }

    public Slider add(Ordinal ordinal) {
        this.ordinalList.add(ordinal);
        refresh();
        return this;
    }

    public Slider range(int min, int max) {
        this.range = new Range().min(min).max(max);
        return this;
    }

    public Slider animation(int interval, boolean loop) {
        this.animation = new Animation().interval(interval).loop(loop);
        return this;
    }

    public Slider bindTo(Collection... collections) {
        this.collections = Arrays.asList(collections);
        return this;
    }

    public Slider onChange(ChangeListener listener) {
        this.changeListener = listener;
        return this;
    }

    @Override
    public void refresh() {
        super.refresh();
        if (notifier == null) return;
        ordinal = ordinalList.size() > 0 ? ordinalList.get(0) : null;
        notifier.refreshOrdinals(ordinals());
    }

    public void update(int value) {
        this.value = value;
        notifyChange();
    }

    private void notifyChange() {
        notifyCollections();
        notifyListener();
    }

    private List<io.intino.alexandria.schemas.Ordinal> ordinals() {
        return ordinalList.stream().map(this::ordinalOf).collect(toList());
    }

    private io.intino.alexandria.schemas.Ordinal ordinalOf(Ordinal ordinal) {
        return new io.intino.alexandria.schemas.Ordinal().label(ordinal.label()).step(ordinal.step());
    }

    private void notifyCollections() {
        collections.forEach(c -> c.filter(timetag()));
    }

    private Timetag timetag() {
        LocalDateTime localDate = Instant.ofEpochMilli(value).atZone(UTC).toLocalDateTime();
        return Timetag.of(localDate, scale());
    }

    private Scale scale() {
        if (ordinal instanceof YearOrdinal) return Scale.Year;
        if (ordinal instanceof MonthOrdinal) return Scale.Month;
        if (ordinal instanceof DayOrdinal) return Scale.Day;
        if (ordinal instanceof HourOrdinal) return Scale.Hour;
        if (ordinal instanceof MinuteOrdinal) return Scale.Minute;
        return Scale.Day;
    }

    private void notifyListener() {
        if (changeListener == null) return;
        changeListener.accept(new ChangeEvent(this, value));
    }
}