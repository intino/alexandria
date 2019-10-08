package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.NumberNotifier;

public class Number<DN extends NumberNotifier, B extends Box> extends AbstractNumber<DN, B> {
    private Double value;
    private Double min;
    private Double max;
    private Double step;

    public Number(B box) {
        super(box);
    }

    public Double value() {
        return value;
    }

    public Double min() {
        return min;
    }

    public Double max() {
        return max;
    }

    public Double step() {
        return step;
    }

    public void value(double value) {
        value(Double.valueOf(value));
    }

    public void value(Double value) {
        _value(value);
        notifier.refresh(value);
    }

    protected Number _value(double value) {
        return _value(Double.valueOf(value));
    }

    protected Number _value(Double value) {
        this.value = value;
        return this;
    }

    protected Number _min(double min) {
        return _min(Double.valueOf(min));
    }

    protected Number _min(Double min) {
        this.min = min;
        return this;
    }

    protected Number _max(double min) {
        return _max(Double.valueOf(min));
    }

    protected Number _max(Double max) {
        this.max = max;
        return this;
    }

    protected Number _step(double step) {
        return _step(Double.valueOf(step));
    }

    protected Number _step(Double step) {
        this.step = step;
        return this;
    }

}