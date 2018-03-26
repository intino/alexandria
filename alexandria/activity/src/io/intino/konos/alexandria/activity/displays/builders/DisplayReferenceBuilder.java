package io.intino.konos.alexandria.activity.displays.builders;

import io.intino.konos.alexandria.activity.schemas.DisplayReference;

public class DisplayReferenceBuilder {

    public static DisplayReference build(String label, String type) {
        return new DisplayReference().label(label).type(type);
    }

}