package io.intino.alexandria.framework.box.displays.builders;

import io.intino.alexandria.framework.box.schemas.DialogReference;

public class DialogReferenceBuilder {

    public static DialogReference build(String location, int width, int height) {
        return new DialogReference().location(location).width(width).height(height);
    }

}