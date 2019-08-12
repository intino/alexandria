package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.NumberNotifier;

public class Number<DN extends NumberNotifier, B extends Box> extends AbstractNumber<DN, B> {
    private double value;
    private double min;
    private double max;
    private double step;

    public Number(B box) {
        super(box);
    }

    public double value() {
        return value;
    }

    public double min() {
        return min;
    }

    public double max() {
        return max;
    }

    public double step() {
        return step;
    }

    public void value(double value) {
        _value(value);
        notifier.refresh(value);
    }

    protected Number _value(double value) {
        this.value = value;
        return this;
    }

    protected Number _min(double min) {
        this.min = min;
        return this;
    }

    protected Number _max(double max) {
        this.max = max;
        return this;
    }

    protected Number _step(double step) {
        this.step = step;
        return this;
    }

}