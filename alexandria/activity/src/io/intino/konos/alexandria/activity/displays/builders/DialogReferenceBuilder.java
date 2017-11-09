package io.intino.konos.alexandria.activity.displays.builders;

import io.intino.konos.alexandria.activity.schemas.DialogReference;

public class DialogReferenceBuilder {

    public static DialogReference build(String location, int width, int height) {
        return new DialogReference().location(location).width(width).height(height);
    }

}