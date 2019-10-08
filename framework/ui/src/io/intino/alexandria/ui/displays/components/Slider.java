package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.slider.Ordinal;
import io.intino.alexandria.ui.displays.notifiers.SliderNotifier;

public class Slider<DN extends SliderNotifier, B extends Box> extends AbstractSlider<DN, B> {

    public Slider(B box) {
        super(box);
    }

    @Override
    public String formattedValue() {
        Ordinal ordinal = ordinal();
        long value = value();
        return ordinal != null ? ordinal.formatter(language()).format(value) : String.valueOf(value);
    }

    @Override
    void updateRange() {
    }

}