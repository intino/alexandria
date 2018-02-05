package io.intino.konos.alexandria.activity.displays.builders;

import io.intino.konos.alexandria.activity.schemas.DialogReference;

public class DialogReferenceBuilder {

    public static DialogReference build(String type, int width, int height) {
        return new DialogReference().type(type).width(width).height(height);
    }

}