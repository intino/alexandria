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

    public Number value(double value) {
        this.value = value;
        return this;
    }

    public double min() {
        return min;
    }

    public Number min(double min) {
        this.min = min;
        return this;
    }

    public double max() {
        return max;
    }

    public Number max(double max) {
        this.max = max;
        return this;
    }

    public double step() {
        return step;
    }

    public Number step(double step) {
        this.step = step;
        return this;
    }

    public void update(double value) {
        value(value);
        notifier.refresh(value);
    }
}