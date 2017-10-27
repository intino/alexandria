package io.intino.konos.server.activity.displays.elements.model;

import io.intino.konos.server.activity.displays.layouts.model.ElementOption;

public class ElementRender {
    private ElementOption option;

    public ElementRender(ElementOption option) {
        this.option = option;
    }

    public ElementOption option() {
        return option;
    }
}
