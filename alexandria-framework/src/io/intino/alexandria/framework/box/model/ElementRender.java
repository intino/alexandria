package io.intino.alexandria.framework.box.model;

import io.intino.alexandria.framework.box.model.layout.ElementOption;

public class ElementRender {
    private ElementOption option;

    public ElementRender(ElementOption option) {
        this.option = option;
    }

    public ElementOption option() {
        return option;
    }
}
