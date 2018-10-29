package io.intino.alexandria.ui.displays.builders;

import io.intino.konos.alexandria.ui.schemas.DisplayReference;

public class DisplayReferenceBuilder {

    public static DisplayReference build(String label, String type) {
        return new DisplayReference().label(label).type(type);
    }

}