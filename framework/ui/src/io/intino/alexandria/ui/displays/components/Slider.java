package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.Selected;
import io.intino.alexandria.ui.displays.components.slider.Ordinal;
import io.intino.alexandria.ui.displays.components.slider.Range;
import io.intino.alexandria.ui.displays.notifiers.SliderNotifier;

public class Slider<DN extends SliderNotifier, B extends Box> extends AbstractSlider<DN, B> {

    public Slider(B box) {
        super(box);
        _value(0);
    }

    @Override
    public void reset() {
        _value(range().min());
    }

    public Slider<DN, B> range(long min, long max) {
        _range(min, max);
        notifier.refreshRange(rangeSchema());
        return this;
    }

    public String formattedValue(long value) {
        return format(value);
    }

    public void moved(long value) {
        notifier.refreshSelected(schemaOf(value));
        notifier.refreshToolbar(toolbarState());
    }

    public void update(long value) {
        value(value);
    }

    @Override
    String format(long value) {
        Ordinal ordinal = ordinal();
        if (ordinal == null) ordinal = defaultOrdinals().get(0);
        return ordinal != null ? ordinal.formatter(language()).format(value) : String.valueOf(value);
    }

    @Override
    void updateRange() {
    }

    @Override
    <T extends Selected> void refreshSelected(T schema) {
        notifier.refreshSelected(schema);
    }

    @Override
    protected void _value(Object value) {
        this.value = value instanceof Integer ? Long.valueOf((int)value) : value;
    }

    @Override
    protected void _range(long min, long max) {
        this.range = new Range().min(min).max(max);
        long value = value();
        if (value < min) _value(min);
        if (value > max) _value(max);
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
    protected boolean canPrevious() {
        return (Long)value() > range.min;
    }

    @Override
    protected boolean canNext() {
        return (Long)value() < range.max;
    }

    @Override
    protected void nextValue() {
        Long value = value();
        if (value >= range.max) {
            if (canLoop()) value(range.min()-1);
            else return;
        }
        value(value +1);
    }

    @Override
    protected boolean checkRange(Object value) {
        if ((Long)value > range.max) return false;
        if ((Long)value < range.min) return false;
        return true;
    }

    @Override
    protected Selected schemaOf(Object value) {
        String formattedValue = formattedValue((Long) value);
        return new Selected().value((Long)value).formattedValue(formattedValue);
    }

}