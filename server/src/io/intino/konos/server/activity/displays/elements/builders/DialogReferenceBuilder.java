package io.intino.konos.server.activity.displays.elements.builders;


import io.intino.konos.server.activity.displays.schemas.DialogReference;

public class DialogReferenceBuilder {

    public static DialogReference build(String location, int width, int height) {
        return new DialogReference().location(location).width(width).height(height);
    }

}