package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.SliderNotifier;

public class Slider<DN extends SliderNotifier, B extends Box> extends AbstractSlider<DN, B> {

    public Slider(B box) {
        super(box);
    }

    @Override
    void updateRange() {
    }

}