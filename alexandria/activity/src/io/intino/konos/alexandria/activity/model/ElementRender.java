package io.intino.konos.alexandria.activity.model;

import io.intino.konos.alexandria.activity.model.layout.ElementOption;

public class ElementRender {
    protected ElementOption option;

    public ElementOption option() {
        return option;
    }

    public ElementRender option(ElementOption option) {
        this.option = option;
        return this;
    }
}
